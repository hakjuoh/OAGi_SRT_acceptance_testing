package org.oagi.srt.uat;

import java.util.Random;

public class CreateCodeListInputs {

    private String name;

    private String agencyId;

    private String version;

    private String definition;

    private String definitionSource;

    private String remark;

    private boolean extensible;

    private String codeListValueCode;

    private String codeListValueShortName;

    private String codeListValueDefinition;

    private String codeListValueDefinitionSource;

    private boolean publish;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDefinitionSource() {
        return definitionSource;
    }

    public void setDefinitionSource(String definitionSource) {
        this.definitionSource = definitionSource;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isExtensible() {
        return extensible;
    }

    public void setExtensible(boolean extensible) {
        this.extensible = extensible;
    }

    public String getCodeListValueCode() {
        return codeListValueCode;
    }

    public void setCodeListValueCode(String codeListValueCode) {
        this.codeListValueCode = codeListValueCode;
    }

    public String getCodeListValueShortName() {
        return codeListValueShortName;
    }

    public void setCodeListValueShortName(String codeListValueShortName) {
        this.codeListValueShortName = codeListValueShortName;
    }

    public String getCodeListValueDefinition() {
        return codeListValueDefinition;
    }

    public void setCodeListValueDefinition(String codeListValueDefinition) {
        this.codeListValueDefinition = codeListValueDefinition;
    }

    public String getCodeListValueDefinitionSource() {
        return codeListValueDefinitionSource;
    }

    public void setCodeListValueDefinitionSource(String codeListValueDefinitionSource) {
        this.codeListValueDefinitionSource = codeListValueDefinitionSource;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    @Override
    public String toString() {
        return "CreateCodeListInputs{" +
                "name='" + name + '\'' +
                ", agencyId='" + agencyId + '\'' +
                ", version='" + version + '\'' +
                ", definition='" + definition + '\'' +
                ", definitionSource='" + definitionSource + '\'' +
                ", remark='" + remark + '\'' +
                ", extensible=" + extensible +
                ", codeListValueCode='" + codeListValueCode + '\'' +
                ", codeListValueShortName='" + codeListValueShortName + '\'' +
                ", codeListValueDefinition='" + codeListValueDefinition + '\'' +
                ", codeListValueDefinitionSource='" + codeListValueDefinitionSource + '\'' +
                ", publish=" + publish +
                '}';
    }

    public static CreateCodeListInputs generateRandomly(Random random) {
        CreateCodeListInputs codeListInputs = new CreateCodeListInputs();

        int randomNo = random.nextInt(10000000);
        String suffix = String.format("%08d", randomNo);

        codeListInputs.setName("codelist_" + suffix);
        codeListInputs.setAgencyId(Integer.toString(random.nextInt(395) + 1));
        codeListInputs.setVersion(Integer.toString(random.nextInt(1000)));
        codeListInputs.setDefinition("codelist_definition_" + suffix);
        codeListInputs.setDefinitionSource("codelist_definition_source_" + suffix);
        codeListInputs.setRemark("codelist_remark_" + suffix);
        codeListInputs.setExtensible(random.nextBoolean());

        codeListInputs.setCodeListValueCode("clv_" + suffix);
        codeListInputs.setCodeListValueShortName("clv_" + suffix);
        codeListInputs.setCodeListValueDefinition("clv_definition_" + suffix);
        codeListInputs.setCodeListValueDefinitionSource("clv_definition_source" + suffix);

        return codeListInputs;
    }
}
