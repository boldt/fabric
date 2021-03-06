package de.uniluebeck.sourcegen.java;

/**
 * Annotation implementation for the annotation of Java class constructors.
 */
public class JConstructorAnnotationImpl extends JElemImpl implements JConstructorAnnotation {

    /**
     * The actual annotation description.
     */
    private final String description;

    /**
     * Generate a constructor annotation with specified description.
     *
     * @param description The actual annotation description.
     */
    public JConstructorAnnotationImpl(String description) {
        this.description = description;
    }

    /**
     * @see de.uniluebeck.sourcegen.ElemImpl#toString(StringBuffer, int)
     */
    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        indent(buffer, tabCount);
        buffer.append("@").append(this.description).append("\n");
    }
}
