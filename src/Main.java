import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import logique.Application;


public class Main extends JFrame
{	
	private Component creerPanelJouer() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Rejoindre une partie existante "));
		
		panel.add(new JLabel("Adresse du serveur : "));
		final JTextField textIP = new JTextField(20);
		panel.add(textIP);
		JButton button = new JButton("Jouer !");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						Application.getInstance().run(new String[] {textIP.getText()});	
					}
				}).start();
				setVisible(false);
			}
		});
		panel.add(button);
		
		return panel;
	}
	
	private Component creerPanelServeur() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Créer un serveur de jeu "));
		
		panel.add(new JLabel("Nombre de joueurs : "));
		final JSpinner nJoueurs = new JSpinner();
		nJoueurs.setModel(new SpinnerNumberModel(1, 1, 6, 1));
		panel.add(nJoueurs);
		JButton button = new JButton("Créer le serveur.");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						Application.getInstance().run(new String[] {"true", String.valueOf((Integer)nJoueurs.getValue())});
					}
				}).start();
				setVisible(false);
			}
		});
		panel.add(button);
		
		return panel;
	}
	
	public Main() {
		JPanel contentPane = new JPanel();
		
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weightx = 1.0;
		contentPane.add(creerPanelJouer(), c);
		contentPane.add(creerPanelServeur(), c);
		
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	public static void main(String args[])
	{
		Main main = new Main();
		main.setVisible(true);
	}
}
