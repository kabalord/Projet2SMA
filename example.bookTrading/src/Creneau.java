public class Creneau {
	
	private int jour; 		
	private int creneau;	
	
	
	public Creneau() {
		super();
		this.jour = (int)(Math.random() * 2 + 1);;
		this.creneau = (int)(Math.random() * 4 + 1);;
	}

	public Creneau(int jour, int creneau) {
		super();
		this.jour = jour;
		this.creneau = creneau;
	}
	
	public int getJour() {
		return jour;
	}
	
	public void setJour(int jour) {
		this.jour = jour;
	}
	
	public int getCreneau() {
		return creneau;
	}
	
	public void setCreneau(int creneau) {
		this.creneau = creneau;
	}

	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + creneau;
		result = prime * result + jour;
		return result;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Creneau other = (Creneau) obj;
		if (creneau != other.creneau)
			return false;
		if (jour != other.jour)
			return false;
		return true;
	}

	
	public String toString() {
		String msg = null;
		switch (creneau) {
		case 1:
			msg =  "À partir de 8h à 10h le" + jour ;
		break;
		case 2:
			msg = "À partir de 10h à 12h le" + jour;
		break;
		case 3:
			msg = "À partir de 14h à 16h le" + jour;
		break;
		case 4:
			msg = " À partir de 16h à 18h le" + jour;
		break;
		}
		return msg;
	}
	

}

