package org.library.business.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringHelperTest {

    private StringHelper helper = new StringHelper();

    private static final String accents = "È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô,Ç,ç,Ã,ã,Õ,õ";
    private static final String expected = "E,E,E,E,U,U,I,I,A,A,O,e,e,e,e,u,u,i,i,a,a,o,C,c,A,a,O,o".toUpperCase();

    private static final String accents2 = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
    private static final String expected2 = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU".toUpperCase();

    private static final String accents3 = "Gisele Bündchen da Conceição e Silva foi batizada assim em homenagem à sua conterrânea de Horizontina, RS.";
    private static final String expected3 = "Gisele Bundchen da Conceicao e Silva foi batizada assim em homenagem a sua conterranea de Horizontina, RS.".toUpperCase();

    private static final String accents4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualização_Diária-1.23.40.exe";
    private static final String expected4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualizacao_Diaria-1.23.40.exe".toUpperCase();

    @Test
    void replacingAllAccents() {

        assertAll(
                () -> assertEquals(expected, helper.cleanUpSpecialChars(accents)),
                () -> assertEquals(expected2, helper.cleanUpSpecialChars(accents2)),
                () -> assertEquals(expected3, helper.cleanUpSpecialChars(accents3)),
                () -> assertEquals(expected4, helper.cleanUpSpecialChars(accents4))
        );


    }


}