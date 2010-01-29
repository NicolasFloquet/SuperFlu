import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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


@SuppressWarnings("serial")
public class Main extends JFrame
{	
	private Component creerPanelJouer() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Rejoindre une partie existante "));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 0;
		
		panel.add(new JLabel("Adresse du serveur : "), c);
		final JTextField textIP = new JTextField(15);
		panel.add(textIP, c);
		JButton button = new JButton("Jouer !");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						Application.getInstance().run(new String[] {textIP.getText()});	
					}
				}).start();
				dispose();
			}
		});
		
		c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.EAST;
		
		panel.add(button, c);
		
		return panel;
	}
	
	private Component creerPanelServeur() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Créer un serveur de jeu "));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 0;
		
		panel.add(new JLabel("Nombre de joueurs : "), c);
		final JSpinner nJoueurs = new JSpinner();
		nJoueurs.setModel(new SpinnerNumberModel(1, 1, 6, 1));
		panel.add(nJoueurs, c);
		JButton button = new JButton("Créer le serveur.");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						Application.getInstance().run(new String[] {"true", String.valueOf((Integer)nJoueurs.getValue())});
					}
				}).start();
				dispose();
			}
		});
		
		c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.EAST;
		
		panel.add(button, c);
		
		return panel;
	}
	
	public Main() {
		JPanel contentPane = new JPanel();
		
		JButton quitterButton = new JButton("Quitter");
		quitterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(creerPanelJouer(), c);
		contentPane.add(creerPanelServeur(), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(50, 0, 0, 0);
		contentPane.add(quitterButton, c);
		
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
