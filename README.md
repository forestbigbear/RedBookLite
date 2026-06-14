# RedBookLite

RedBookLite 是一个基于 XML、Fragment、MVVM、Repository 的简易图文发布与推荐流 Android 项目。当前版本在保留原有首页、详情页、发布页和个人页能力的基础上，补充了网络层、状态 UI、分类筛选、搜索、发布校验、作者主页和点赞持久化。

## 功能说明

- 首页推荐流：RecyclerView + StaggeredGridLayoutManager 瀑布流展示图文笔记。
- Retrofit 网络请求：通过 Retrofit + Gson Converter 请求推荐流 DTO，并映射为本地 `Note`。
- 本地与网络协同：优先保留内存缓存；网络刷新成功后更新缓存，失败时保留已有本地数据并展示错误。
- Coil 图片加载：支持本地文件、drawable 种子图和网络 URL。
- 下拉刷新：首页使用 SwipeRefreshLayout 触发推荐流刷新。
- Loading / 空态 / 错误态：首页支持加载中、空列表、网络错误和重试。
- 网络异常展示：网络请求失败时显示错误文案并提供重试按钮。
- 分类筛选：首页支持 `全部`、`travel`、`food`、`fashion`。
- 搜索：首页支持按标题关键字过滤。
- 发布笔记：支持选择封面、标题校验、正文校验、封面校验，发布成功后写入内存缓存。
- 个人主页：底部“我”页面展示自己发布的笔记。
- 作者主页：点击作者头像或昵称进入作者页，展示该作者帖子。
- 详情页：展示大图、标题、作者、正文和点赞数，点赞后实时更新。
- 点赞持久化：使用 SharedPreferences 持久化点赞数，不引入 Room。
- 网络头像：接口提供 `author.avatar_url` 时优先加载网络头像，否则使用本地头像兜底。

## 项目结构

```text
app/src/main
├─ AndroidManifest.xml
├─ java
│  ├─ com/example/redbooklite
│  │  ├─ MainActivity.kt
│  │  ├─ RedBookApp.kt
│  │  ├─ data
│  │  │  ├─ local
│  │  │  │  ├─ NoteLocalStore.kt
│  │  │  │  ├─ SeedData.kt
│  │  │  │  └─ LikePreferenceStore.kt
│  │  │  ├─ remote
│  │  │  │  ├─ FeedApiService.kt
│  │  │  │  ├─ NetworkModule.kt
│  │  │  │  ├─ NoteDto.kt
│  │  │  │  └─ NoteDtoMapper.kt
│  │  │  └─ repository
│  │  │     └─ NoteRepository.kt
│  │  └─ ui
│  │     ├─ author
│  │     ├─ detail
│  │     ├─ feed
│  │     └─ publish
│  ├─ model
│  │  └─ Note.kt
│  ├─ profile
│  │  ├─ ProfileFragment.kt
│  │  └─ ProfileViewModel.kt
│  └─ util
│     ├─ AvatarHelper.kt
│     ├─ CoverLayoutHelper.kt
│     ├─ FileHelper.kt
│     ├─ ImageLoader.kt
│     └─ ImageSizeHelper.kt
└─ res
   ├─ layout
   ├─ drawable
   ├─ drawable-nodpi
   └─ values
```

## 技术架构说明

- UI 层：Activity / Fragment 只负责界面渲染、用户事件转发和页面跳转。
- ViewModel 层：管理页面状态、输入校验、筛选、搜索、刷新等 UI 相关状态。
- Repository 层：统一协调内存缓存、网络请求、DTO 映射、发布、点赞、作者过滤等数据逻辑。
- 本地数据：`NoteLocalStore` 使用内存 `StateFlow` 保存运行期笔记列表；`LikePreferenceStore` 使用 SharedPreferences 保存点赞数。
- 网络数据：`FeedApiService` 定义 Retrofit 接口，`NoteDtoMapper` 将 DTO 转为领域模型 `Note`。
- 图片加载：`ImageLoader` 封装 Coil 和 drawable 加载逻辑。

## 运行方法

1. 使用 Android Studio 打开项目根目录 `D:\RedBookLite`。
2. 等待 Gradle Sync 完成。
3. 连接模拟器或真机。
4. 点击 Android Studio 的 Run，或在项目根目录执行：

```powershell
.\gradlew.bat :app:assembleDebug
```

生成的 Debug APK 位于：

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 测试步骤

1. 启动 App，确认默认进入“发现”首页。
2. 首页下拉刷新，网络失败时应看到错误文案和“重试”按钮，原本地数据不丢失。
3. 点击分类按钮 `travel`、`food`、`fashion`，确认列表按分类过滤。
4. 在搜索框输入标题关键字，确认列表按标题过滤。
5. 点击笔记卡片进入详情页，确认大图、标题、作者、正文、点赞数展示正常。
6. 在详情页点击点赞，返回后再次进入详情页，确认点赞数保留；重启 App 后点赞数仍保留。
7. 点击底部中间发布按钮，尝试不选封面、不填标题、不填正文，确认校验提示。
8. 完整填写发布内容并发布，确认返回后新笔记进入列表。
9. 点击底部“我”，确认展示自己发布的笔记。
10. 点击首页作者头像或昵称，确认进入作者主页并展示该作者帖子。

## 已知说明

- `NetworkModule` 当前使用占位地址 `https://example.com/redbooklite/feed.json`，可替换为课程提供的真实接口。
- 没有引入 Room；点赞持久化按要求单独使用 SharedPreferences。
- 网络头像仅在接口 DTO 提供 `author.avatar_url` 时使用，否则回退到本地头像。
