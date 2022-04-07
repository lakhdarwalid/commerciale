import javax.swing.SwingUtilities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.commerciale.controller.CRUDuser;
import com.commerciale.model.hibernateUtil;
import com.commerciale.model.user;
import com.commerciale.view.createUser;
import com.commerciale.view.loginfrm;

public class Commerciale {

	public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
		
		@Override
		public void run() {
			loginfrm logfrm = new loginfrm();
			logfrm.setVisible(true);
			
		}
	});	
		
		
		
	}

}
