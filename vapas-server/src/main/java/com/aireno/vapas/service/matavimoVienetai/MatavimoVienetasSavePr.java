package com.aireno.vapas.service.matavimoVienetai;

import com.aireno.dto.*;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.persistance.MatavimoVienetas;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class MatavimoVienetasSavePr extends ProcessorBase<MatavimoVienetasSaveReq, MatavimoVienetasSaveResp> {


    @Override
    protected MatavimoVienetasSaveResp processInt(MatavimoVienetasSaveReq request) throws Exception {
        MatavimoVienetasDtoMap mapper = new MatavimoVienetasDtoMap();
        MatavimoVienetasSaveResp resp = new MatavimoVienetasSaveResp();
        getAssertor().isNotNull(request.item, "Nėra įrašo");
        getAssertor().isNotNullStr(request.item.getKodas(), "Nėra kodo");
        getAssertor().isNotNullStr(request.item.getPavadinimas(), "Nėra pavadinimo");
        MatavimoVienetas item = new MatavimoVienetas();
        if (request.item.getId() > 0){
            String queryString = "from MatavimoVienetas c where c.id = ?1";
            List<MatavimoVienetas> list =  getSession().createQuery(queryString)
                    .setParameter("1", request.item.getId()).list();
            getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
            item =  list.get(0);
        }
        mapper.fromDto(item, request.item);
        getSession().save(item);
        resp.result = mapper.toDto(item);
        return resp;
    }

}
