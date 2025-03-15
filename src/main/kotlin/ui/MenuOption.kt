package ui

enum class MenuOption(
    val description: String,
) {
    EXIT("Выйти"),
    SHOW_ALL_SONGS("Показать список песен"),
    CREATE_PLAYLIST("Создать плейлист"),
    REMOVE_PLAYLIST("Удалить плейлист"),
    SHOW_ALL_PLAYLISTS("Показать все плейлисты"),
    SHOW_PLAYLIST("Вывести плейлист"),
    PLAY_PLAYLIST("Включить плейлист"),
    ADD_SONG_TO_PLAYLIST("Добавить песню в плейлист"),
    REMOVE_SONG_FROM_PLAYLIST("Убрать песню из плейлиста"),
    PLAY_PREVIOUS_TRACK("Включить предыдущую песню"),
    PLAY_NEXT_TRACK("Включить следующую песню"),
    REPLAY_CURRENT_TRACK("Повторить текущую песню"),
    PAUSE_RESUME_TRACK("Поставить на паузу/возобновить воспроизведение"),
    ;

    val id: Int = ordinal

    companion object {
        fun fromId(id: Int): MenuOption? = entries.find { it.id == id }
    }
}
