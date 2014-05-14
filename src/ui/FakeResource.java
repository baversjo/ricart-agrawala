package ui;

public class FakeResource {
	public static void access(){
		System.out.println("Resource was accessed by " + Main.PROCESS_ID);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	}
}
