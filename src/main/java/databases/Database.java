package databases;

public abstract class Database implements DatabaseInterface, AutoCloseable {
    private boolean resourceUsed = false;

    public void useResource() {
        resourceUsed = true;
    }

    @Override
    public void close() throws IllegalStateException {
        if (!resourceUsed) {
            throw new IllegalStateException("Resource not used in try-with-resources block");
        }
        resourceUsed = false;
    }
}
