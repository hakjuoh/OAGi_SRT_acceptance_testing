package org.oagi.srt.uat.testcase;

import java.util.Random;

public class CreateContextSchemeInputs {

    private String contextCategory;

    private String name;

    private String schemeId;

    private String agencyId;

    private String version;

    private String description;

    private String value;

    private String meaning;

    public String getContextCategory() {
        return contextCategory;
    }

    public void setContextCategory(String contextCategory) {
        this.contextCategory = contextCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "CreateContextSchemeInputs{" +
                "contextCategory='" + contextCategory + '\'' +
                ", name='" + name + '\'' +
                ", schemeId='" + schemeId + '\'' +
                ", agencyId='" + agencyId + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }

    public static CreateContextSchemeInputs generateRandomly(Random random, String ctxCatName) {
        CreateContextSchemeInputs contextSchemeInputs = new CreateContextSchemeInputs();
        contextSchemeInputs.setContextCategory(ctxCatName);

        int randomNo = random.nextInt(10000000);
        String suffix = String.format("%08d", randomNo);

        contextSchemeInputs.setName("test_ctx_sch_name_" + suffix);
        contextSchemeInputs.setSchemeId("test_ctx_sch_schemeId_" + suffix);
        contextSchemeInputs.setAgencyId("test_ctx_agencyId_" + suffix);
        contextSchemeInputs.setVersion("test_ctx_version_" + suffix);
        contextSchemeInputs.setDescription("test_ctx_sch_description_" + suffix);

        contextSchemeInputs.setValue("test_ctx_sch_value_" + suffix);
        contextSchemeInputs.setMeaning("test_ctx_sch_meaning_" + suffix);

        return contextSchemeInputs;
    }
}
