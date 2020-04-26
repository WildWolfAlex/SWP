
public class AluradDecorator extends FahrzeugDecorator{
	public AluradDecorator(IFahrzeug fahrzeug) {
		super(fahrzeug);
	}

	@Override
	public String getName() {
		return super.fahrzeug.getName()+" mit Alur�der;";
	}

	@Override
	public int getPreis() {
		return super.fahrzeug.getPreis()+1000;
	}
	public String toString() {
		return super.fahrzeug.toString()+" mit Alur�der;";
	}
}
