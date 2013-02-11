package com.aireno.vapas.service.matavimoVienetai;

import com.aireno.dto.MatavimoVienetasGautiReq;
import com.aireno.dto.MatavimoVienetasGautiResp;
import com.aireno.dto.MatavimoVienetasSaveReq;
import com.aireno.dto.MatavimoVienetasSaveResp;
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
public class MatavimoVienetasGautiPr extends ProcessorBase<MatavimoVienetasGautiReq, MatavimoVienetasGautiResp> {


    @Override
    protected MatavimoVienetasGautiResp processInt(MatavimoVienetasGautiReq request) throws Exception {
        MatavimoVienetasDtoMap mapper = new MatavimoVienetasDtoMap();
        MatavimoVienetasGautiResp resp = new MatavimoVienetasGautiResp();

            String queryString = "from MatavimoVienetas c where c.id = ?1";
            List<MatavimoVienetas> list =  getSession().createQuery(queryString)
                    .setParameter("1", request.id).list();
            getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
        MatavimoVienetas item =  list.get(0);
        resp.result = mapper.toDto(item);
        return resp;
    }
}
