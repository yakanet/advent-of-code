package common

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Puzzle(val year: Int, val day: Int, val mockIndex: Int = 0)
