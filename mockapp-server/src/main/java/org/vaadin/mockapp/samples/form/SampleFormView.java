package org.vaadin.mockapp.samples.form;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.mockapp.samples.data.MockData;
import org.vaadin.mockapp.samples.data.SampleEnum;
import org.vaadin.mockapp.samples.data.SampleMaster;

import java.util.UUID;

/**
 * @author petter@vaadin.com
 */
public class SampleFormView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "sampleFormView";
    @PropertyId("uuid")
    private TextField uuid;
    @PropertyId("stringProperty")
    private TextField stringField;
    @PropertyId("integerProperty")
    private TextField integerField;
    @PropertyId("bigDecimalProperty")
    private TextField bigDecimalField;
    @PropertyId("booleanProperty")
    private CheckBox booleanField;
    @PropertyId("embeddedProperty.enumProperty")
    private ComboBox enumField;
    private BeanFieldGroup<SampleMaster> binder;

    public SampleFormView() {
        setMargin(true);
        final FormLayout formLayout = new FormLayout();
        addComponent(formLayout);

        formLayout.addComponent(uuid = new TextField("UUID"));
        uuid.setConverter(new UUIDToStringConverter());
        formLayout.addComponent(stringField = new TextField("String"));
        stringField.addValidator(new StringLengthValidator("The string must be between 10 and 30 characters", 10, 30, false));
        formLayout.addComponent(integerField = new TextField("Integer"));
        integerField.addValidator(new IntegerRangeValidator("The integer must be positive", 0, Integer.MAX_VALUE));
        formLayout.addComponent(bigDecimalField = new TextField("BigDecimal"));
        bigDecimalField.setConverter(new BigDecimalToStringConverter());
        formLayout.addComponent(booleanField = new CheckBox("Boolean"));
        formLayout.addComponent(enumField = new ComboBox("Enum"));
        for (SampleEnum sampleEnum : SampleEnum.values()) {
            enumField.addItem(sampleEnum);
        }

        binder = new BeanFieldGroup<SampleMaster>(SampleMaster.class);
        binder.bindMemberFields(this);
    }

    private void bind(SampleMaster master) {
        uuid.setReadOnly(false);
        final BeanItem<SampleMaster> item = new BeanItem<SampleMaster>(master);
        item.addNestedProperty("embeddedProperty.enumProperty");
        binder.setItemDataSource(item);
        uuid.setReadOnly(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        SampleMaster masterToBind = null;
        try {
            UUID uuid = UUID.fromString(event.getParameters());
            masterToBind = MockData.getInstance().getMasterRecordByUuid(uuid);
        } catch (IllegalArgumentException ex) {
        }
        if (masterToBind == null) {
            masterToBind = new SampleMaster();
        }
        bind(masterToBind);
    }
}
