package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_sequence_global.xsd
 */
public class CT_Sequence_Global_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_Sequence_Global_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override
    void generateClasses() throws Exception {
        /*
         * CarType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName("CarType")
                .addElement("int", "HorsePower")
                .addElement("String", "LicenseNumber")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());
        /*
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
