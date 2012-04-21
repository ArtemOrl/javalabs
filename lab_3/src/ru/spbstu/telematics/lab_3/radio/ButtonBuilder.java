package ru.spbstu.telematics.lab_3.radio;

/**
 * Строитель конпки, позволяющий "делать" кнопки для радио.
 */
public class ButtonBuilder {

	/**
	 * @param id - идентификатор (имя) кнопки.
	 * @return реализацию интерфейса {@link IRadioButton} с определенным
	 * методом {@link IRadioButton#click(IRadio)}, который выполняет какую-то функцию радио.
	 */
	public IRadioButton makeButton(String id)
	{
		if(id == "ON") {
			return new IRadioButton() {
				@Override
				public void click(IRadio radio) {
					radio.on();
				}
			};
		}
		else if(id == "OFF") {
			return new IRadioButton() {
				@Override
				public void click(IRadio radio) {
					radio.off();
				}
			};
		}
		else if(id == "SCAN") {
			return new IRadioButton() {
				@Override
				public void click(IRadio radio) {
					radio.scan();
				}
			};
		}
		else if(id == "RESET") {
			return new IRadioButton() {
				@Override
				public void click(IRadio radio) {
					radio.reset();
				}
			};
		}
		return null;
	}
}
