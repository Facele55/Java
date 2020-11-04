import java.util.HashMap;

public class Cesar
{
    static HashMap<Character, Character> cipher = new HashMap<>();
    public static void main(String[] args) {
        String text = "JavaTheBest!";
        loadCipher();   
        System.out.println("Original text : ");
        System.out.println(text);  
        System.out.println("encrypting...");
        String encrypted = encrypt(text);
        System.out.println(encrypted);
        //Lets decrypt the encrypted  text.
        System.out.println("decrypting...");
        String decrypted = decrypt(encrypted);
        System.out.println(decrypted);
    }
    
    static String encrypt(String str)
    {
        String lowerCase = str.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for(int index=0; index<lowerCase.length();index++)
        {
            if(cipher.get(lowerCase.charAt(index))==null)
            sb.append(lowerCase.charAt(index));
        else
            sb.append(cipher.get(lowerCase.charAt(index)));
        }
        return sb.toString();
    }
    
    static String decrypt(String str)
    {
        String lowerCase = str.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for(int index = 0; index< str.length();index++)
        {
           if(getKeyFromValue(lowerCase.charAt(index))!=null)     sb.append(getKeyFromValue(lowerCase.charAt(index)));  
       else
           sb.append(lowerCase.charAt(index));
        }
        return sb.toString();
    }
    
    static Character getKeyFromValue(char c)
    {
        for(char ch: cipher.keySet())
            {
                if(c==cipher.get(ch))
                    return ch;
            }
        return null;
    }
    
    static void loadCipher()
    {
        //key = decripted
        //value = encripted
        String keys = "abcdefghijklmnopqrstuvwxyz";
        String values = "bcdefghijklmnopqrstuvwxyza";
        for(int index=0;index<keys.length();index++)
        {
            cipher.put(keys.charAt(index),values.charAt(index));
        }
    }
}