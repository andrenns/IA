import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class SensorCor
{
	EV3ColorSensor colorSensor;
	SampleProvider colorProvider;
	float [] sample;
	
	public SensorCor()
	{
		Port s2 = LocalEV3.get().getPort("S2");
		colorSensor = new EV3ColorSensor(s2);
		colorProvider = colorSensor.getColorIDMode();
		sample = new float [colorProvider.sampleSize()];
	}

	public EV3ColorSensor getColorSensor() {
		return colorSensor;
	}

	public void setColorSensor(EV3ColorSensor colorSensor) {
		this.colorSensor = colorSensor;
	}

	public SampleProvider getColorProvider() {
		return colorProvider;
	}

	public void setColorProvider(SampleProvider colorProvider) {
		this.colorProvider = colorProvider;
	}

	public float[] getSample() {
		return sample;
	}

	public void setSample(float[] sample) {
		this.sample = sample;
	}

	
}