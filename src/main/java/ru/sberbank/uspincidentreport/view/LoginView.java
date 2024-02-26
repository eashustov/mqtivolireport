package ru.sberbank.uspincidentreport.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
//        addClassName("login-rich-content");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        getThemeList().add(Lumo.DARK);
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);

        H2 logo_1 = new H2("Управление инфраструктурой серверов приложений:");
        H2 logo_2 = new H2("Технологические инциденты системы мониторинга ДСП");
        logo_2.getStyle().set("margin-top", "1px");
//        getStyle().set("background-color", "var(--lumo-contrast-5pct)")
//                .set("display", "flex").set("justify-content", "center")
//                .set("padding", "var(--lumo-space-l)");

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Вход");
        i18nForm.setUsername("Пользователь");
        i18nForm.setPassword("Пароль");
        i18nForm.setSubmit("Вход");
        i18nForm.setForgotPassword("Забыли пароль?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Неверное имя пользователя или пароль");
        i18nErrorMessage.setMessage(
                "Введите корректные данные пользователя и пароль и попробуйте снова.");
        i18n.setErrorMessage(i18nErrorMessage);

        loginForm.setI18n(i18n);
        add(logo_1, logo_2, loginForm);
        // Prevent the example from stealing focus when browsing the
        // documentation
        loginForm.getElement().setAttribute("no-autofocus", "");

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
