package com.example.redbooklite.data.local

import android.content.Context
import androidx.annotation.DrawableRes
import com.example.redbooklite.R
import com.example.redbooklite.model.Note
import com.example.redbooklite.util.ImageSizeHelper

object SeedData {

    private data class SeedDraft(
        val title: String,
        val content: String,
        @DrawableRes val coverResId: Int,
        val authorName: String,
        val likeCount: Int,
        val minutesAgo: Long
    )

    /** 生成种子笔记，封面比例从 drawable 自动读取 */
    fun buildNotes(context: Context): List<Note> {
        val now = System.currentTimeMillis()
        return drafts().mapIndexed { index, draft ->
            Note(
                id = (index + 1).toLong(),
                title = draft.title,
                content = draft.content,
                coverPath = ImageSizeHelper.drawableCoverPath(draft.coverResId),
                coverAspectRatio = ImageSizeHelper.readAspectRatioFromDrawable(
                    context,
                    draft.coverResId
                ),
                authorName = draft.authorName,
                likeCount = draft.likeCount,
                isMine = false,
                createdAt = now - 1000 * 60 * draft.minutesAgo
            )
        }
    }

    private fun drafts(): List<SeedDraft> {
        return listOf(
            SeedDraft(
                title = "外滩打卡！和好友的魔都夏日",
                content = "陆家嘴天际线太绝了，江风一吹，什么烦恼都没了。和兄弟们在上海的第一张合影，收藏！",
                coverResId = R.drawable.seed_cover_1,
                authorName = "旅行阿杰",
                likeCount = 328,
                minutesAgo = 30
            ),
            SeedDraft(
                title = "窗外的上海，蓝调时刻太浪漫",
                content = "坐在窗边看苏州河上的游船缓缓驶过，外滩源的夜景真的像电影画面。这一刻只想让时间慢下来。",
                coverResId = R.drawable.seed_cover_2,
                authorName = "城市漫游者",
                likeCount = 512,
                minutesAgo = 90
            ),
            SeedDraft(
                title = "仿佛走进了一幅水墨画",
                content = "晨雾缭绕中的古塔与小桥流水，每一处风景都像诗里的世界。不用远行，也能找到心灵的栖息地。",
                coverResId = R.drawable.seed_cover_3,
                authorName = "古风拾光",
                likeCount = 891,
                minutesAgo = 150
            ),
            SeedDraft(
                title = "这抹蒂芙尼蓝，治愈了整个夏天",
                content = "湖水清澈到能看见湖底的木头，雪山和彩林倒映在水面。大自然的调色盘，真的不需要滤镜。",
                coverResId = R.drawable.seed_cover_4,
                authorName = "山野旅人",
                likeCount = 1204,
                minutesAgo = 210
            ),
            SeedDraft(
                title = "雪山脚下的蒂芙尼蓝，美到失语",
                content = "阳光洒在雪峰上，冰川湖像一颗镶嵌在山谷里的宝石。所有的疲惫，都在看见这片湖的瞬间消散了。",
                coverResId = R.drawable.seed_cover_5,
                authorName = "远方日记",
                likeCount = 1567,
                minutesAgo = 280
            ),
            SeedDraft(
                title = "多洛米蒂的黄昏，像童话里的小镇",
                content = "尖峰、绿坡、白墙教堂，金色夕阳把整片山谷染暖。这是我把在欧洲最想再来的地方写进清单的原因。",
                coverResId = R.drawable.seed_cover_6,
                authorName = "欧洲漫步",
                likeCount = 943,
                minutesAgo = 360
            ),
            SeedDraft(
                title = "周末的夜晚，就要在街头小酌",
                content = "树灯、条纹遮阳伞、杯盏碰撞的声响，城市的烟火气大概就是这样。和好友聊聊天，比任何计划都治愈。",
                coverResId = R.drawable.seed_cover_7,
                authorName = "慢生活研究所",
                likeCount = 426,
                minutesAgo = 420
            ),
            SeedDraft(
                title = "今日份好心情，简单妆容就出门",
                content = "红唇指甲是小小的仪式感，黑色上衣百搭不出错。记录一下平凡日子里，也要认真爱自己的瞬间。",
                coverResId = R.drawable.seed_cover_8,
                authorName = "小梨日记",
                likeCount = 612,
                minutesAgo = 480
            ),
            SeedDraft(
                title = "通勤路上的随手拍，风衣是主角",
                content = "人潮里也能拥有自己的节奏。米色风衣配金色细链，低调但很耐看，早秋穿搭就按这个公式来。",
                coverResId = R.drawable.seed_cover_9,
                authorName = "都市丽人",
                likeCount = 389,
                minutesAgo = 540
            ),
            SeedDraft(
                title = "夜晚微醺，沙发上的松弛感",
                content = "暖光、香槟色连衣裙、细高跟，不需要去很远的地方，在家附近找一家安静的酒吧就能重启心情。",
                coverResId = R.drawable.seed_cover_10,
                authorName = "晚风与酒",
                likeCount = 758,
                minutesAgo = 600
            ),
            SeedDraft(
                title = "樱花季限定，粉色外套太应景了",
                content = "桥下的水面落满花瓣，像粉色的地毯。春天最浪漫的事，大概就是穿一身浅色系来赴一场花约。",
                coverResId = R.drawable.seed_cover_11,
                authorName = "春日旅拍",
                likeCount = 1024,
                minutesAgo = 660
            ),
            SeedDraft(
                title = "和闺蜜出街，棕色系穿搭好高级",
                content = "一个美拉德运动风，一个灰色休闲风，走在街上回头率拉满。阳光正好的下午，最适合一起扫街拍照。",
                coverResId = R.drawable.seed_cover_12,
                authorName = "姐妹穿搭局",
                likeCount = 867,
                minutesAgo = 720
            ),
            SeedDraft(
                title = "紫色球场的少年感，今天很帅",
                content = "灰色宽松T恤配红色球鞋，在薰衣草色的球场上运球，夏天的风都是自由的味道。运动也可以很潮。",
                coverResId = R.drawable.seed_cover_13,
                authorName = "球场日记",
                likeCount = 531,
                minutesAgo = 780
            ),
            SeedDraft(
                title = "城市夜色里，随手一拍都好看",
                content = "霓虹灯映在脸上，街道还有晚风。不需要刻意找景点，下班后的散步就是最好的治愈时刻。",
                coverResId = R.drawable.seed_cover_14,
                authorName = "夜行少年",
                likeCount = 445,
                minutesAgo = 840
            ),
            SeedDraft(
                title = "赴一场花约，春天真的来了",
                content = "石桥、落樱、浅粉外套，把整个人都衬得温柔。人虽然多，但抬头看见满树繁花的那一刻，还是觉得很值得。",
                coverResId = R.drawable.seed_cover_15,
                authorName = "樱花记事本",
                likeCount = 1189,
                minutesAgo = 900
            ),
            SeedDraft(
                title = "周末逛街，给自己挑一件小首饰",
                content = "白色系带上衣配亮闪闪的项链和戒指，站在柜台前试戴的时候，心情也跟着亮了起来。犒劳自己，从来不需要理由。",
                coverResId = R.drawable.seed_cover_16,
                authorName = "精致日常",
                likeCount = 673,
                minutesAgo = 960
            ),
            SeedDraft(
                title = "上海夜晚街拍，黑色系永远耐看",
                content = "路灯、梧桐树、路过的电车，还有手边这只小包。All black 也能在夜色里很有存在感，今晚的城市格外温柔。",
                coverResId = R.drawable.seed_cover_17,
                authorName = "魔都夜猫子",
                likeCount = 902,
                minutesAgo = 1020
            ),
            SeedDraft(
                title = "公园二十分钟，晒晒太阳就很开心",
                content = "白T恤、绿植、斑驳光影，最简单的搭配反而最耐看。夏天就要清清爽爽，把节奏放慢一点。",
                coverResId = R.drawable.seed_cover_18,
                authorName = "清爽男孩",
                likeCount = 356,
                minutesAgo = 1080
            )
        )
    }
}