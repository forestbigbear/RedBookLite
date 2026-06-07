package com.example.redbooklite.data.local

import com.example.redbooklite.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 笔记的内存数据源。
 *
 * 职责：在 App 运行期间保存「当前所有笔记」列表，并提供增删改查。
 * 不负责：首页排序、筛选「我的」、组装发布字段等业务（由 [com.example.redbooklite.data.repository.NoteRepository] 处理）。
 *
 */
class NoteLocalStore {

    /**
     * 内部持有的笔记列表（可变）。
     *
     * - 类型为 [MutableStateFlow]，表示「会多次发射新值的列表」；
     * - 使用 `private`，只允许本类通过 [saveAll]、[add]、[update] 修改，避免外部随意改数据；
     * - 初始值为空列表，启动后由 Repository 调用 [saveAll] 写入种子数据。
     */
    private val notesState = MutableStateFlow<List<Note>>(emptyList())

    /**
     * 对外暴露的只读笔记流。
     *
     * - 由 [notesState.asStateFlow] 得到，外部只能订阅、不能 `.value =` 直接赋值；
     * - [com.example.redbooklite.data.repository.NoteRepository] 订阅此 Flow，列表变化时首页/我的页会自动更新；
     * - 与 [notesState] 指向同一份列表数据，改 [notesState] 后 [notesFlow] 会同步发出新列表。
     */
    val notesFlow: StateFlow<List<Note>> = notesState.asStateFlow()

    /**
     * 下一条新笔记建议使用的 id（自增）。
     *
     * 发布笔记时通过 [generateNextId] 获取；[saveAll] 整表替换时会按列表中最大 id 重新校准，
     * 避免种子数据或批量写入后 id 冲突。
     */
    private var nextId = 1L

    /**
     * 判断当前是否还没有任何笔记。
     *
     * 典型用法：App 启动时 Repository 若发现为空，则写入 [com.example.redbooklite.data.local.SeedData] 种子列表。
     *
     * @return `true` 表示内存列表为空；`false` 表示至少有一条笔记。
     */
    fun isEmpty(): Boolean = notesState.value.isEmpty()

    /**
     * 生成并返回一个新的笔记 id，同时把内部计数器加 1。
     *
     * 保证连续发布多条笔记时 id 不重复（在单次进程生命周期内）。
     * 一般由 Repository 在 [publishNote] 时调用，不把 id 生成逻辑放在 UI 层。
     *
     * @return 本次应分配给新笔记的 id。
     */
    fun generateNextId(): Long {
        val id = nextId
        nextId++
        return id
    }

    /**
     * 用新列表**整体替换**当前内存中的笔记列表。
     *
     * 会同时：
     * 1. 更新 [notesState]，从而通过 [notesFlow] 通知所有订阅方；
     * 2. 根据新列表中的最大 id 重置 [nextId]，供后续发布使用。
     *
     * 典型场景：
     * - 首次启动写入种子数据；
     * - [add]、[update] 内部也会间接调用本方法提交新列表。
     *
     * @param notes 替换后的完整列表（不是增量追加）。
     */
    fun saveAll(notes: List<Note>) {
        notesState.value = notes
        nextId = (notes.maxOfOrNull { it.id } ?: 0L) + 1L
    }

    /**
     * 在现有列表末尾**追加**一条笔记。
     *
     * 实现方式：读取当前列表 + 新笔记，再调用 [saveAll] 写回。
     * 发布成功后，订阅 [notesFlow] 的页面会收到包含新笔记的列表。
     *
     * @param note 要追加的笔记（应已包含合法 id，通常由 Repository 使用 [generateNextId] 生成）。
     */
    fun add(note: Note) {
        saveAll(notesState.value + note)
    }

    /**
     * 按笔记 id **更新**列表中已存在的一条记录。
     *
     * 遍历当前列表，若某条 [Note.id] 与参数 [note.id] 相同则用新对象替换，其余保持不变，
     * 最后通过 [saveAll] 写回。若找不到对应 id，则列表内容不变（等同于未更新）。
     *
     * 典型场景：详情页点赞后，将 `likeCount + 1` 的 [Note] 写回内存。
     *
     * @param note 包含相同 id 的完整新数据（通常用 `copy(likeCount = ...)` 生成）。
     */
    fun update(note: Note) {
        val newList = notesState.value.map { old ->
            if (old.id == note.id) note else old
        }
        saveAll(newList)
    }

    /**
     * 根据 id 查询单条笔记。
     *
     * 只读内存列表，不修改 [notesState]。详情页加载、点赞前取旧数据等场景由 Repository 调用。
     *
     * @param noteId 笔记主键 id。
     * @return 找到则返回对应 [Note]；不存在则返回 `null`（调用方应处理，例如关闭详情页）。
     */
    fun getById(noteId: Long): Note? {
        return notesState.value.find { it.id == noteId }
    }
}