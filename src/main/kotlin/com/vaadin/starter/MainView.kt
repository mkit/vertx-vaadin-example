package com.vaadin.starter

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import io.vertx.core.Handler
import io.vertx.core.Vertx
import java.time.LocalDateTime

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@CssImport.Container(
    CssImport("./styles/shared-styles.css"),
    CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
)
@Push
class MainView : VerticalLayout() {
    val label = Span()
    init {
        // Use TextField for standard text input
        val textField =
            TextField("Your name")

        // Button click listeners can be defined as lambda expressions
        val greetService = GreetService()
        val button = Button("Say hello",
            ComponentEventListener {
                Notification.show(
                    greetService.greet(textField.value)
                )
            }
        )

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER)

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content")
        add(textField, button, label)
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        val ui = attachEvent?.ui;
        val vertx = Vertx.currentContext().owner()
        val timerId = vertx.setPeriodic(1000) { ui?.access { label.text = LocalDateTime.now().toString() } }
        addDetachListener { vertx.cancelTimer(timerId) }
    }
}
