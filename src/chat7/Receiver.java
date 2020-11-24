<<<<<<< HEAD
package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

//서버가 보내는 Echo메시지를 읽어오는 쓰레드 클래스
public class Receiver extends Thread{
	
	Socket socket;
	BufferedReader in = null;
	
	public Receiver(Socket socket) {
		this.socket = socket;
		
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		}
		catch (Exception e) {
			System.out.println("예외>Receiver>생성자: "+e);
		}
	} //Receiver
	
	@Override
	public void run() {
		//스트림을 통해 서버가 보낸 내용을 라인단위로 읽어온다.
		while(in != null ) {
			
			try {
				System.out.println("Thread Receive: "+in.readLine()); //in.readLine() thread가 하는 일
			} 
			catch (SocketException e) {
				System.out.println("SocketException 발생됨. 루프 탈출");
				break;
			}
			catch (Exception e) {
				/*
				 클라이언트가 접속을 종료할 경우 SocketException이 발생되면서
				 무한루프에 빠지게 된다.
				 */
				System.out.println("예외>Receiver>run1: "+e);
			}
		}
		
		try {
			in.close();
		} 		
		catch (Exception e) {
			System.out.println("예외>Receiver>run2: "+e);
		}		
	}
}
=======
package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

//서버가 보내는 Echo메시지를 읽어오는 쓰레드 클래스
public class Receiver extends Thread{
	
	Socket socket;
	BufferedReader in = null;
	
	public Receiver(Socket socket) {
		this.socket = socket;
		
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		}
		catch (Exception e) {
			System.out.println("예외>Receiver>생성자: "+e);
		}
	} //Receiver
	
	@Override
	public void run() {
		//스트림을 통해 서버가 보낸 내용을 라인단위로 읽어온다.
		while(in != null ) {
			
			try {
				System.out.println("Thread Receive: "+in.readLine()); //in.readLine() thread가 하는 일
			} 
			catch (SocketException e) {
				System.out.println("SocketException 발생됨. 루프 탈출");
				break;
			}
			catch (Exception e) {
				/*
				 클라이언트가 접속을 종료할 경우 SocketException이 발생되면서
				 무한루프에 빠지게 된다.
				 */
				System.out.println("예외>Receiver>run1: "+e);
			}
		}
		
		try {
			in.close();
		} 		
		catch (Exception e) {
			System.out.println("예외>Receiver>run2: "+e);
		}		
	}
}
>>>>>>> refs/remotes/origin/master
