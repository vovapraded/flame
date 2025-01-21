package backend.academy.transform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransformerNamed {
    DISK(new DiskTransformer()),
    HEART(new HeartTransformer()),
    LINEAR(new LinearTransformer()),
    POLAR(new PolarTransformer()),
    SINUSOIDAL(new SinusoidalTransformer()),
    SPHERICAL(new SphericalTransformer());
    private final Transformer transformer;
}
