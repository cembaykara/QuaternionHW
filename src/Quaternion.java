import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Quaternions. Basic operations. */
public class Quaternion {

    private final double a, b, c, d;

    /** Constructor from four double values.
     * @param a real part
     * @param b imaginary part i
     * @param c imaginary part j
     * @param d imaginary part k
     */
    public Quaternion(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /** Real part of the quaternion.
     * @return real part
     */
    public double getRpart() {
        return a;
    }

    /** Imaginary part i of the quaternion.
     * @return imaginary part i
     */
    public double getIpart() {
        return b;
    }

    /** Imaginary part j of the quaternion.
     * @return imaginary part j
     */
    public double getJpart() {
        return c;
    }

    /** Imaginary part k of the quaternion.
     * @return imaginary part k
     */
    public double getKpart() {
        return d;
    }

    /**
     * Conversion of the quaternion to the string.
     *
     * @return a string form of this quaternion:
     * "a+bi+cj+dk"
     * (without any brackets)
     */
    @Override
    public String toString() {

        Double[] values = {b,c,d};
        ArrayList<String> strValues = new ArrayList<>();

        for (Double value : values){
            if (value < 0){
                strValues.add(value.toString());
            }else {
                strValues.add("+" + value.toString());
            }
        }
        return a + strValues.get(0) + "i" + strValues.get(1) + "j" + strValues.get(2) + "k";
    }

    /** Conversion from the string to the quaternion.
     * Reverse to <code>toString</code> method.
     * @throws IllegalArgumentException if string s does not represent
     *     a quaternion (defined by the <code>toString</code> method)
     * @param s string of form produced by the <code>toString</code> method
     * @return a quaternion represented by string s
     */
    public static Quaternion valueOf (String s) {

        String format = "^[-+]?\\d+\\.?\\d?+[-+]\\d+\\.?\\d?+i[-+]\\d+\\.?\\d?+j[-+]\\d+\\.?\\d?+k$";

        if (s.matches(format)){

            ArrayList<Double> values = new ArrayList<>();
            Pattern p = Pattern.compile("-?\\d+\\.?\\d?+");
            Matcher m = p.matcher(s);

            try {

                while (m.find()){
                   values.add(Double.parseDouble(m.group(0)));
                }

            } catch(Exception e) {

            }

            return new Quaternion(values.get(0), values.get(1), values.get(2), values.get(3));

        }else {
            throw new IllegalArgumentException("Format must be 'a+bi+cj+dk'. Input is: " + s);
        }
    }

    /** Clone of the quaternion.
     * @return independent clone of <code>this</code>
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Quaternion(a,b,c,d);
    }

    /** Test whether the quaternion is zero.
     * @return true, if the real part and all the imaginary parts are (close to) zero
     */
    public boolean isZero() {
        Quaternion quaternion = new Quaternion(0,0,0,0);
        if(this.equals(quaternion)){
            return true;
        }else {
            return false;
        }
    }

    /** Create zero Quaternion.
     * @return new zero quaternion
     */
    public static Quaternion zero(){
        return new Quaternion(0,0,0,0);
    }

    /** Conjugate of the quaternion. Expressed by the formula
     *     conjugate(a+bi+cj+dk) = a-bi-cj-dk
     * @return conjugate of <code>this</code>
     */
    public Quaternion conjugate() {
        return new Quaternion(a, -b,-c,-d);
    }

    /** Opposite of the quaternion. Expressed by the formula
     *    opposite(a+bi+cj+dk) = -a-bi-cj-dk
     * @return quaternion <code>-this</code>
     */
    public Quaternion opposite() {
        return new Quaternion(-a,-b,-c,-d);
    }

    /** Sum of quaternions. Expressed by the formula
     *    (a1+b1i+c1j+d1k) + (a2+b2i+c2j+d2k) = (a1+a2) + (b1+b2)i + (c1+c2)j + (d1+d2)k
     * @param q addend
     * @return quaternion <code>this+q</code>
     */
    public Quaternion plus (Quaternion q) {
        return new Quaternion(a + q.a, b + q.b, c + q.c, d + q.d);
    }

    /** Product of quaternions. Expressed by the formula
     *  (a1+b1i+c1j+d1k) * (a2+b2i+c2j+d2k) = (a1a2-b1b2-c1c2-d1d2) + (a1b2+b1a2+c1d2-d1c2)i +
     *  (a1c2-b1d2+c1a2+d1b2)j + (a1d2+b1c2-c1b2+d1a2)k
     * @param q factor
     * @return quaternion <code>this*q</code>
     */
    public Quaternion times (Quaternion q) {

        final double real = this.a*q.a - this.b*q.b - this.c*q.c - this.d*q.d;
        final double i = this.a*q.b + this.b*q.a + this.c*q.d - this.d*q.c;
        final double j = this.a*q.c - this.b*q.d + this.c*q.a + this.d*q.b;
        final double k = this.a*q.d + this.b*q.c - this.c*q.b + this.d*q.a;

        return new Quaternion(real, i , j, k);
    }

    /** Multiplication by a coefficient.
     * @param r coefficient
     * @return quaternion <code>this*r</code>
     */
    public Quaternion times (double r) {
        return new Quaternion(a*r, b*r, c*r, d*r);
    }

    /** Inverse of the quaternion. Expressed by the formula
     *     1/(a+bi+cj+dk) = a/(a*a+b*b+c*c+d*d) +
     *     ((-b)/(a*a+b*b+c*c+d*d))i + ((-c)/(a*a+b*b+c*c+d*d))j + ((-d)/(a*a+b*b+c*c+d*d))k
     * @return quaternion <code>1/this</code>
     */
    public Quaternion inverse() {

        if (this.isZero()){
            throw new RuntimeException("Zero inverse found!");
        }

        final double real = a/(a*a+b*b+c*c+d*d);
        final double i = (-b)/(a*a+b*b+c*c+d*d);
        final double j = (-c)/(a*a+b*b+c*c+d*d);
        final double k = (-d)/(a*a+b*b+c*c+d*d);

        return new Quaternion(real, i, j, k);
    }

    /** Difference of quaternions. Expressed as addition to the opposite.
     * @param q subtrahend
     * @return quaternion <code>this-q</code>
     */
    public Quaternion minus (Quaternion q) {
        return new Quaternion(a - q.a, b - q.b, c - q.c, d - q.d);
    }

    /** Right quotient of quaternions. Expressed as multiplication to the inverse.
     * @param q (right) divisor
     * @return quaternion <code>this*inverse(q)</code>
     */
    public Quaternion divideByRight (Quaternion q) {
        try {
            return this.times(q.inverse());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage() + " Can't divide quaternion by a zero quaternion. " + "Input is "
                    + q.toString());
        }
    }

    /** Left quotient of quaternions.
     * @param q (left) divisor
     * @return quaternion <code>inverse(q)*this</code>
     */
    public Quaternion divideByLeft (Quaternion q) {
        try {
            return q.inverse().times(this);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage() + " Can't divide quaternion by a zero quaternion." + "Input is "
                    + q.toString());
        }
    }

    /** Equality test of quaternions. Difference of equal numbers
     *     is (close to) zero.
     * @param qo second quaternion
     * @return logical value of the expression <code>this.equals(qo)</code>
     */
    @Override
    public boolean equals (Object qo) {

        if(qo instanceof Quaternion) {
            if(compare(this.a, ((Quaternion) qo).getRpart()) && compare(this.b, ((Quaternion) qo).getIpart()) && compare(this.c, ((Quaternion) qo).getJpart()) && compare(this.d, ((Quaternion) qo).getKpart()))
                return true;
        }
        return false;
    }

    /** Dot product of quaternions. (p*conjugate(q) + q*conjugate(p))/2
     * @param q factor
     * @return dot product of this and q
     */
    public Quaternion dotMult (Quaternion q) {
        return this.times(q.conjugate()).plus(q.times(this.conjugate())).times(0.5);
    }

    /** Integer hashCode has to be the same for equal objects.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /** Norm of the quaternion. Expressed by the formula
     *     norm(a+bi+cj+dk) = Math.sqrt(a*a+b*b+c*c+d*d)
     * @return norm of <code>this</code> (norm is a real number)
     */
    public double norm() {
        return Math.sqrt(a*a + b*b + c*c + d*d);
    }

    public static boolean compare(double a, double b){

        if(Math.abs(a-b)<0.0001)
            return true;

        return false;
    }

    /** Main method for testing purposes.
     * @param arg command line parameters
     */
    public static void main (String[] arg) {

        Quaternion arv1 = new Quaternion (12., -34., 1., 5.);
        Quaternion zeroQuaternion = Quaternion.zero();

        if (arg.length > 0)
            arv1 = valueOf (arg[0]);
        System.out.println ("first: " + arv1.toString());
        System.out.println ("real: " + arv1.getRpart());
        System.out.println ("imagi: " + arv1.getIpart());
        System.out.println ("imagj: " + arv1.getJpart());
        System.out.println ("imagk: " + arv1.getKpart());
        System.out.println ("isZero: " + arv1.isZero());
        System.out.println ("conjugate: " + arv1.conjugate());
        System.out.println ("opposite: " + arv1.opposite());
        System.out.println ("hashCode: " + arv1.hashCode());
        Quaternion res = null;
        try {
            res = (Quaternion)arv1.clone();
        } catch (CloneNotSupportedException e) {};
        System.out.println ("clone equals to original: " + res.equals (arv1));
        System.out.println ("clone is not the same object: " + (res!=arv1));
        System.out.println ("hashCode: " + res.hashCode());
        res = valueOf (arv1.toString());
        System.out.println ("string conversion equals to original: "
                + res.equals (arv1));
        Quaternion arv2 = new Quaternion (1., -2.,  -1., 2.);
        if (arg.length > 1)
            arv2 = valueOf (arg[1]);
        System.out.println ("second: " + arv2.toString());
        System.out.println ("hashCode: " + arv2.hashCode());
        System.out.println ("equals: " + arv1.equals (arv2));
        res = arv1.plus (arv2);
        System.out.println ("plus: " + res);
        System.out.println ("times: " + arv1.times (arv2));
        System.out.println ("minus: " + arv1.minus (arv2));
        double mm = arv1.norm();
        System.out.println ("norm: " + mm);
        System.out.println ("inverse: " + arv1.inverse());
        System.out.println ("divideByRight: " + arv1.divideByRight (arv2));
        System.out.println ("divideByLeft: " + arv1.divideByLeft (arv2));
        System.out.println (valueOf("-1-2i+3j-4.5k"));
    }

}
// end of file
