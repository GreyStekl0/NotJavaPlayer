package ui

class MenuDisplay {
    fun displayMenu() {
        println("\nВыберите действие:")
        MenuOption.entries.forEach { option ->
            if (option != MenuOption.EXIT) {
                println("${option.id}) ${option.description}")
            }
        }
        println("${MenuOption.EXIT.id}) ${MenuOption.EXIT.description}\n")
    }
}
