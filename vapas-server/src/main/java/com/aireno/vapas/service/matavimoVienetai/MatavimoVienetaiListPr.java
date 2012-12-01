package com.aireno.vapas.service.matavimoVienetai;

import com.aireno.dto.MatavimoVienetaiListReq;
import com.aireno.dto.MatavimoVienetaiListResp;
import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.persistance.MatavimoVienetas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class MatavimoVienetaiListPr extends ProcessorBase<MatavimoVienetaiListReq, MatavimoVienetaiListResp> {


    @Override
    protected MatavimoVienetaiListResp processInt(MatavimoVienetaiListReq request) {
        List<MatavimoVienetas> result = getSession().createQuery( "from MatavimoVienetas" ).list();
        MatavimoVienetasDtoMap mapper = new MatavimoVienetasDtoMap();
        MatavimoVienetaiListResp resp = new MatavimoVienetaiListResp();
        resp.result = new ArrayList<MatavimoVienetasDto>();
        for(MatavimoVienetas item: result)
        {
            resp.result.add(mapper.toDto(item));
        }
        return resp;
    }
}
