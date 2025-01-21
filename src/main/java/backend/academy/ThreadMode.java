package backend.academy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ThreadMode {
    MULTI_THREAD,
    SINGLE_THREAD;
}
