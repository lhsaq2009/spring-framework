package example.gh24375;

import org.springframework.stereotype.Component;

@Component
@EnclosingAnnotation(nested2 = @NestedAnnotation)
public class AnnotatedComponent {
}
