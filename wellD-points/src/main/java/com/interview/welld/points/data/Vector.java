package com.interview.welld.points.data;

import java.util.Objects;

public class Vector {
	private Double i;
	private Double j;
	
	public Vector() {}

	public Double getI() {
		return i;
	}

	public void setI(Double i) {
		this.i = i;
	}

	public Double getJ() {
		return j;
	}

	public void setJ(Double j) {
		this.j = j;
	}
	
	public void normalize() {
		Double size = Math.sqrt(this.i*this.i+this.j*this.j);
		this.setI(this.i/size);
		this.setJ(this.j/size);
	}

	@Override
	public int hashCode() {
		return Objects.hash(i, j);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		return Objects.equals(i, other.i) && Objects.equals(j, other.j);
	}
}