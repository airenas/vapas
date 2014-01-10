package com.aireno.vapas.service;

import com.aireno.dto.PrekeDto;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.Nustatymas;
import com.aireno.vapas.service.persistance.Preke;
import com.aireno.vapas.service.prekes.PrekeDtoMap;

import java.util.List;

/**
 * @author Airenas Vaičiūnas
 * @since 14.1.10 (10.39)
 */
public class NustatymasServiceImpl extends ServiceBase implements NustatymasService {

    @Override
    public NustatymasResponce<Boolean> gautiBoolean(final NustatymasRequest<Boolean> request) throws Exception {
        return new NustatymasProcessor<Boolean>() {
            @Override
            protected Boolean convert(String reiksme) {
                return Boolean.parseBoolean(reiksme);
            }
        }.process(request);
    }

    @Override
    public NustatymasResponce<Boolean> saugotiBoolean(NustatymasSaugotiRequest<Boolean> request) throws Exception {
        return new NustatymasSaugotiProcessor<Boolean>() {
        }.process(request);
    }

    static abstract class NustatymasProcessor<T> extends ProcessorBase<NustatymasRequest<T>, NustatymasResponce<T>> {
        @Override
        protected NustatymasResponce<T> processInt(NustatymasRequest<T> request) throws Exception {
            NustatymasResponce<T> result = new NustatymasResponce<>();
            getAssertor().isNotNullStr(request.name, "Nenurodytas nustatymo kodas");
            String queryString = "from Nustatymas c where c.kodas = ?1";
            List<Nustatymas> list = getSession().createQuery(queryString)
                    .setParameter("1", request.name).list();
            getAssertor().isTrue(list.size() < 2, "Keli nustatymo įrašai '%s'", request.name);
            if (list.size() == 1) {
                result.result = convert(list.get(0).getReiksme());
            } else {
                result.result = request.defaultValue;
            }
            return result;
        }

        protected abstract T convert(String reiksme);
    }

    static abstract class NustatymasSaugotiProcessor<T> extends ProcessorBase<NustatymasSaugotiRequest<T>, NustatymasResponce<T>> {
        @Override
        protected NustatymasResponce<T> processInt(NustatymasSaugotiRequest<T> request) throws Exception {
            NustatymasResponce<T> result = new NustatymasResponce<>();
            getAssertor().isNotNullStr(request.name, "Nenurodytas nustatymo kodas");
            getAssertor().isNotNull(request.value, "Nėra reikšmės");
            Nustatymas item = new Nustatymas();
            List<Nustatymas> list = getSession().createQuery("from Nustatymas c where c.kodas = ?1")
                    .setParameter("1", request.name).list();
            getAssertor().isTrue(list.size() < 2, "Keli nustatymo įrašai '%s'", request.name);
            if (list.size() == 1) {
                item = list.get(0);
            }
            item.setKodas(request.name);
            item.setReiksme(request.value.toString());
            getSession().save(item);
            result.result = request.value;
            return result;
        }
    }
}
