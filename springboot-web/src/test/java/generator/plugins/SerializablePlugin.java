package generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class SerializablePlugin extends PluginAdapter {

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		addSerializable(topLevelClass,introspectedTable);
		return super.modelBaseRecordClassGenerated(topLevelClass,introspectedTable);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		addSerializable(topLevelClass,introspectedTable);
		return super.modelExampleClassGenerated(topLevelClass,introspectedTable);
	}

	private void addSerializable(TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
		topLevelClass.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
		topLevelClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
		Field field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setFinal(true);
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("long"));
		field.setName("serialVersionUID");
		field.setInitializationString("1L");
		topLevelClass.addField(field);
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

}
