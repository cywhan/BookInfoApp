package inha.teamproject

class StringsearchActivity_add {
    var isSelected: Boolean = false
    var animal: String? = null

    fun getAnimals(): String {
        return this!!.animal.toString()
    }

    fun setAnimals(animal: String) {
        this.animal = animal
    }

    fun getSelecteds(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }
}