package neuralnetz;

import java.io.Serializable;
import java.util.ArrayList;

import matrix.BadMatrix;
import matrix.LengthMismatch;
import matrix.Matrix;

public class Neuronalnetz implements Serializable{
	public float[] inputn;
	ArrayList<float[]> hln=new ArrayList<float[]>();
	ArrayList<Matrix> weights=new ArrayList<Matrix>();
	ArrayList<float[]> biases = new ArrayList<float[]>();
	public float[] outputn;
	/**
	 * @param inputn Array der Inputneuronen
	 * @param hln Hidden Layer ArrayList
	 * @param weights Alle Gewichtematrixen, l�nge der Arraylist muss um 1 gr��er sein als der hln-Array
	 * @param biases Biasmatrix. L�ngen m�ssen mit HiddenLayer-anzahl und L�nge �bereinstimmen
	 * @param outputn Array der Outputneuronen
	 */
	public Neuronalnetz(float[] inputn, ArrayList<float[]> hln, ArrayList<Matrix> weights, ArrayList<float[]> biases, float[] outputn) {
		this.inputn=inputn;
		this.hln=hln;
		this.weights=weights;
		this.biases=biases;
		this.outputn=outputn;
	}
	/**
	 * 
	 * @param in Anzahl der Inputneuronen
	 * @param hl Anzahl und L�nge der Hidden-Layer
	 * @param ou Anzahl der Outputneuronen
	 * @param grange Wertebereich der Gewichte
	 * @param brange Wertebereich der Biases
	 */
	public Neuronalnetz(float in[], int[] hl, int ou,float grange, float brange) {
		//inputn=new float[in];
		outputn=new float[ou];
		int lb=in.length;
		for(int i=0;i<hl.length;i++) {
			Matrix m=new Matrix(hl[i],lb);
			for(int j=0;j<hl[i];j++) {
				for(int k=0;k<lb;k++) {
					m.mat[j][k]=(float)Math.random()*grange-grange/2;
					
				}
			}
			weights.add(m);
			lb=hl[i];
			float[] b=new float[lb];
			for(int j=0;j<lb;j++) {
				b[j]=(float)(Math.random()*brange-brange/2);
			}
			biases.add(b);
		}
		Matrix m=new Matrix(ou,lb);
		for(int j=0;j<ou;j++) {
			for(int k=0;k<lb;k++) {
				m.mat[j][k]=(float)(Math.random()*grange-grange/2);
				
			}
		}
		weights.add(m);
	}
	public void randomIn(float[] in) {
		for(int i=0;i<in.length;i++) {
			in[i]=(float) Math.random()*1000-500;
		}
	}
	public void cycle() throws LengthMismatch, BadMatrix {
		float[] prev=inputn;
		//System.out.println(weights.size()-1);
		for(int i=0;i<weights.size()-1;i++) {
			//System.out.println(weights.get(i).c);
			//System.out.println(prev.length);
			float[] temphln=Matrix.vertMult(weights.get(i), prev);
			for(int j=0;j<temphln.length;j++) {
				
				temphln[j]=Matrix.sigmoidFunc((float)(temphln[j]+biases.get(i)[j]));
			}
			hln.add(temphln);
			prev=temphln;
		}
		float[] temphln=Matrix.vertMult(weights.get(weights.size()-1), prev);
		for(int i=0;i<temphln.length;i++) {
			temphln[i]=Matrix.sigmoidFunc((float)(temphln[i]));
		}
		outputn=temphln;
		
	}
	/**
	 * 
	 * @param n Netz, mit dem gepaart werden soll
	 * @param pvalue Gibt an wie viel vom jeweiligen Netz �bernommen werden soll (2 entspricht 50% von beiden,
	 *  h�here Werte bedeutet mehr vom Netz n wird �bernommen
	 * @param mutvalue Gibt an, wie gro� die Auswirkungen einer Mutation sein k�nnen.
	 * @return
	 */
	public Neuronalnetz paaren(Neuronalnetz n,float pvalue, float mutvalue) {
		ArrayList<Matrix> weightsneu=new ArrayList<Matrix>();
		ArrayList<float[]> biasesneu = new ArrayList<float[]>();
		for(int i=0;i<weights.size();i++) {
			Matrix mn=new Matrix(weights.get(i).mat.length,weights.get(i).mat[0].length);
			for(int j=0;j<weights.get(i).mat.length;j++) {
				
				for(int k=0;k<weights.get(i).mat[j].length;k++) {
					if(j%pvalue!=0) {
						float zuf=(float)Math.random();
						if(zuf<=0.05) {
							mn.mat[j][k]=this.weights.get(i).mat[j][k]+(float)(Math.random()*mutvalue-mutvalue/2);
						}
						else {
						mn.mat[j][k]=this.weights.get(i).mat[j][k];
						}
					}
					else {
						float zuf=(float)Math.random();
						if(zuf<=0.05) {
							mn.mat[j][k]=n.weights.get(i).mat[j][k]+(float)(Math.random()*mutvalue-mutvalue/2);
						}
						else {
						mn.mat[j][k]=n.weights.get(i).mat[j][k];
						}
					}
				}
			}
			weightsneu.add(mn);
		}	
		for(int i=0;i<biases.size();i++) {
			float[] nb=new float[biases.get(i).length];
			for(int j=0;j<biases.get(i).length;j++) {
				if(j%2!=0) {
					nb[j]=this.biases.get(i)[j];
				}
				else {
					nb[j]=n.biases.get(i)[j];
				}
			}
			biasesneu.add(nb);
		}
		return new Neuronalnetz(new float[this.inputn.length],new ArrayList<float[]>(),weightsneu,biasesneu,new float[this.outputn.length]);
	}
	public static void main(String[] args) throws LengthMismatch, BadMatrix {
		/*int[] a= {10,5,4};
		Neuronalnetz n=new Neuronalnetz(210,a,4,2,2);
		Neuronalnetz m=new Neuronalnetz(210,a,4,2,2);
		n.randomIn(n.inputn);
		m.inputn=n.inputn;
		n.cycle();
		m.cycle();
		for(int i=0;i<n.outputn.length;i++) {
			System.out.printf(" %f;", n.outputn[i]);
		}
		System.out.println();
		for(int i=0;i<m.outputn.length;i++) {
			System.out.printf(" %f;", m.outputn[i]);
		}
		System.out.println();
		Neuronalnetz o=n.paaren(m,2,1);
		o.inputn=n.inputn;
		o.cycle();
		for(int i=0;i<o.outputn.length;i++) {
			System.out.printf(" %f;", o.outputn[i]);
		}*/
	}

}
