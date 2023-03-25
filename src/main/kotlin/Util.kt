fun <T> MutableCollection<T>.addIfNotNull(value: T?) {
    if (value != null) {
        add(value)
    }
}