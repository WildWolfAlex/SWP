
public class IchNixVerstehenState implements IState {

	@Override
	public void goNext(Context c) {
			c.setState(new AufnahmeState());

	}

	@Override
	public void printMsg() {
		System.out.println("Wie bitte?");

	}

}
