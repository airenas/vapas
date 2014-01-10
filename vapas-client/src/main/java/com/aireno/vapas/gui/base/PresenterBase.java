
package com.aireno.vapas.gui.base;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.ClassBase;
import com.aireno.base.Processor;
import com.aireno.vapas.gui.main.MainPresenter;
import com.aireno.vapas.service.LookupService;
import com.aireno.vapas.service.NustatymasService;
import jfxtras.labs.dialogs.DialogFX;

public abstract class PresenterBase extends ClassBase implements GuiPresenter {
    GuiPresenter from;
    protected long id;
    protected boolean initializing = false;

    boolean initDone = false;

    public LookupService getLookupService() {
        return ApplicationContextProvider.getProvider().getBean(LookupService.class);
    }

    public NustatymasService getNustatymasService() {
        return ApplicationContextProvider.getProvider().getBean(NustatymasService.class);
    }

    public void show(String presenter) {
        getMainPresenter().show(presenter);
    }

    protected void show(String presenter, long id) {
        getMainPresenter().show(presenter, id);
    }


    protected MainPresenter getMainPresenter() {
        return ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
    }

    protected <T> T getBean(Class<T> cls) {
        return ApplicationContextProvider.getProvider().getBean(cls);
    }

    protected <T, TResp> Processor<T, TResp> getProcessor(Class<T> reqClass) {
        return ApplicationContextProvider.getProvider().getBean(reqClass.getSimpleName(), Processor.class);
    }

    public void setText(String info) {
        getMainPresenter().setText(info);
    }

    public void setTitle(String info) {
        getMainPresenter().setTitle(info);
    }

    public void goBack() {
        if (from != null)
            getMainPresenter().show(from);
    }

    @Override
    public void from(GuiPresenter currentPresenter) {
        from = currentPresenter;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void update() {
        getMainPresenter().update();
    }

    @Override
    public void updateControls() {
    }

    @Override
    public boolean needSave() {
        return false;
    }

    @Override
    public void save() throws Exception {
    }

    public boolean init() throws Exception {
        initializing = true;
        try {
            try {
                if (!initDone) {
                    initOnce();
                    initDone = true;
                }
                initInternal();

            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
                return false;
            }


        } finally {
            initializing = false;
        }
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    //protected abstract void initInternal() throws Exception;

    protected void initOnce() throws Exception {

    }

    protected void initInternal() throws Exception {

    }

    public void setError(String msg, Throwable e) {
        DialogFX dialog = new DialogFX(DialogFX.Type.ERROR);
        dialog.setTitleText("Klaida");
        dialog.setMessage(msg + e.getLocalizedMessage());
        getLog().error(msg, e);
        dialog.showDialog();
    }

    public String getTitle() {
        return "Vaistų apskaita";
    }

    protected void runAndExpectError(Runnable run, String what) {
        try {
            run.run();
        } catch (Exception e) {
            setError(what, e);
        }
    }
}
