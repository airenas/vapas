package com.aireno.vapas.service;

import com.aireno.dto.LikutisListDto;

import java.util.List;

/**
 *
 * @author Airenas Vaičiūnas
 * @since 14.1.10 (10.24)
 */
public interface NustatymasService
{
    NustatymasResponce<Boolean> gautiBoolean(NustatymasRequest<Boolean> request) throws Exception;

    NustatymasResponce<Boolean> saugotiBoolean(NustatymasSaugotiRequest<Boolean> request) throws Exception;

    public class NustatymasResponce<T> {
        public T result;
    }

    class NustatymasRequest<T> {
        public String name;
        public T defaultValue;

        public NustatymasRequest(String name, T defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }
    }

    class NustatymasSaugotiRequest<T> {
        public String name;
        public T value;

        public NustatymasSaugotiRequest(String name, T value) {
            this.name = name;
            this.value = value;
        }
    }
}
