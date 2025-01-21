package backend.academy.pixel;

public record Color(int red, int green, int blue) {

    public Color mixWith(Color other) {
        return new Color((red + other.red()) / 2, (green + other.green) / 2, (blue + other.blue) / 2);
    }
}
