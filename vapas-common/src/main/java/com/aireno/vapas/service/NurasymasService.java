package com.aireno.vapas.service;

import com.aireno.dto.NurasymasDto;
import com.aireno.dto.NurasymasListDto;

import java.util.List;

public interface NurasymasService {
    List<NurasymasListDto> sarasas(String[] keywords) throws Exception;

    NurasymasDto gauti(Long id) throws Exception;

    NurasymasDto saugoti(NurasymasDto updatedContact) throws Exception;

    Boolean tvirtinti(Long id) throws Exception;

    Boolean generuotiAtaskaita(long id) throws Exception;
}
