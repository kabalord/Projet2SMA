import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gui extends JFrame{
	private Enseignant myAgent;
	
	private JTextField JourField, GroupeField, CreneauField;
	
	Gui(Enseignant a) {
		super(a.getLocalName());
		
		myAgent = a;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 3));
		
		p.add(new JLabel("jour :"));
		JourField = new JTextField(15);
		p.add(JourField);
		
		p.add(new JLabel("Groupe :"));
		GroupeField = new JTextField(15);
		p.add(GroupeField);
		
		p.add(new JLabel("creneau :"));
		CreneauField  = new JTextField(15);
		p.add(CreneauField);
		
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton confirmerButton = new JButton("confirmer");
		confirmerButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String Jour = JourField.getText().trim();
					String Groupe = GroupeField.getText().trim();
					String Creneau = CreneauField.getText().trim();
					//myAgent.updateCatalogue(title, Integer.parseInt(price));
					
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(Gui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
			
		} );
		
		p = new JPanel();
		p.add(confirmerButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner	
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		} );
		
		setResizable(false);
	}
	
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 3;
		int centerY = (int)screenSize.getHeight() / 3;
		setLocation(centerX - getWidth() / 3, centerY - getHeight() / 3);
		super.setVisible(true);
	}	
	
}

