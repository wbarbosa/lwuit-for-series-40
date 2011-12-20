/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package javax.microedition.lcdui;
/**
 * A TextField is an editable text component that may be placed into a Form. It can be given a piece of text that is used as the initial value.
 * A TextField has a maximum size, which is the maximum number of characters that can be stored in the object at any time (its capacity). This limit is enforced when the TextField instance is constructed, when the user is editing text within the TextField, as well as when the application program calls methods on the TextField that modify its contents. The maximum size is the maximum stored capacity and is unrelated to the number of characters that may be displayed at any given time. The number of characters displayed and their arrangement into rows and columns are determined by the device.
 * The implementation may place a boundary on the maximum size, and the maximum size actually assigned may be smaller than the application had requested. The value actually assigned will be reflected in the value returned by getMaxSize(). A defensively-written application should compare this value to the maximum size requested and be prepared to handle cases where they differ.
 * The TextField shares the concept of input constraints with the TextBox class. The different constraints allow the application to request that the user's input be restricted in a variety of ways. The implementation is required to restrict the user's input as requested by the application. For example, if the application requests the NUMERIC constraint on a TextField, the implementation must allow only numeric characters to be entered.
 * The actual contents of the text object are set and modified by and are reported to the application through the TextBox and TextField APIs. The displayed contents may differ from the actual contents if the implementation has chosen to provide special formatting suitable for the text object's constraint setting. For example, a PHONENUMBER field might be displayed with digit separators and punctuation as appropriate for the phone number conventions in use, grouping the digits into country code, area code, prefix, etc. Any spaces or punctuation provided are not considered part of the text object's actual contents. For example, a text object with the PHONENUMBER constraint might display as follows:
 * but the actual contents of the object visible to the application through the APIs would be the string 4085551212. The size method reflects the number of characters in the actual contents, not the number of characters that are displayed, so for this example the size method would return 10.
 * Some constraints, such as DECIMAL, require the implementation to perform syntactic validation of the contents of the text object. The syntax checking is performed on the actual contents of the text object, which may differ from the displayed contents as described above. Syntax checking is performed on the initial contents passed to the constructors, and it is also enforced for all method calls that affect the contents of the text object. The methods and constructors throw IllegalArgumentException if they would result in the contents of the text object not conforming to the required syntax.
 * The value passed to the setConstraints() method consists of a restrictive constraint setting described above, as well as a variety of flag bits that modify the behavior of text entry and display. The value of the restrictive constraint setting is in the low order 16 bits of the value, and it may be extracted by combining the constraint value with the CONSTRAINT_MASK constant using the bit-wise AND () operator. The restrictive constraint settings are as follows:
 * The modifier flags reside in the high order 16 bits of the constraint value, that is, those in the complement of the CONSTRAINT_MASK constant. The modifier flags may be tested individually by combining the constraint value with a modifier flag using the bit-wise AND () operator. The modifier flags are as follows:
 * The TextField shares the concept of input modes with the TextBox class. The application can request that the implementation use a particular input mode when the user initiates editing of a TextField or TextBox. The input mode is a concept that exists within the user interface for text entry on a particular device. The application does not request an input mode directly, since the user interface for text entry is not standardized across devices. Instead, the application can request that the entry of certain characters be made convenient. It can do this by passing the name of a Unicode character subset to the setInitialInputMode() method. Calling this method requests that the implementation set the mode of the text entry user interface so that it is convenient for the user to enter characters in this subset. The application can also request that the input mode have certain behavioral characteristics by setting modifier flags in the constraints value.
 * The requested input mode should be used whenever the user initiates the editing of a TextBox or TextField object. If the user had changed input modes in a previous editing session, the application's requested input mode should take precedence over the previous input mode set by the user. However, the input mode is not restrictive, and the user is allowed to change the input mode at any time during editing. If editing is already in progress, calls to the setInitialInputMode method do not affect the current input mode, but instead take effect at the next time the user initiates editing of this text object.
 * The initial input mode is a hint to the implementation. If the implementation cannot provide an input mode that satisfies the application's request, it should use a default input mode.
 * The input mode that results from the application's request is not a restriction on the set of characters the user is allowed to enter. The user MUST be allowed to switch input modes to enter any character that is allowed within the current constraint setting. The constraint setting takes precedence over an input mode request, and the implementation may refuse to supply a particular input mode if it is inconsistent with the current constraint setting.
 * For example, if the current constraint is ANY, the call
 * should set the initial input mode to allow entry of uppercase Latin characters. This does not restrict input to these characters, and the user will be able to enter other characters by switching the input mode to allow entry of numerals or lowercase Latin letters. However, if the current constraint is NUMERIC, the implementation may ignore the request to set an initial input mode allowing MIDP_UPPERCASE_LATIN characters because these characters are not allowed in a TextField whose constraint is NUMERIC. In this case, the implementation may instead use an input mode that allows entry of numerals, since such an input mode is most appropriate for entry of data under the NUMERIC constraint.
 * A string is used to name the Unicode character subset passed as a parameter to the setInitialInputMode() method. String comparison is case sensitive.
 * Unicode character blocks can be named by adding the prefix UCB_ to the the string names of fields representing Unicode character blocks as defined in the J2SE class java.lang.Character.UnicodeBlock. Any Unicode character block may be named in this fashion. For convenience, the most common Unicode character blocks are listed below.
 * Input subsets as defined by the J2SE class java.awt.im.InputSubset may be named by adding the prefix IS_ to the string names of fields representing input subsets as defined in that class. Any defined input subset may be used. For convenience, the names of the currently defined input subsets are listed below.
 * MIDP has also defined the following character subsets:
 * Finally, implementation-specific character subsets may be named with strings that have a prefix of X_. In order to avoid namespace conflicts, it is recommended that implementation-specific names include the name of the defining company or organization after the initial X_ prefix.
 * For example, a Japanese language application might have a particular TextField that the application intends to be used primarily for input of words that are loaned from languages other than Japanese. The application might request an input mode facilitating Hiragana input by issuing the following method call:
 * Implementations need not compile in all the strings listed above. Instead, they need only to compile in the strings that name Unicode character subsets that they support. If the subset name passed by the application does not match a known subset name, the request should simply be ignored without error, and a default input mode should be used. This lets implementations support this feature reasonably inexpensively. However, it has the consequence that the application cannot tell whether its request has been accepted, nor whether the Unicode character subset it has requested is actually a valid subset.
 * Since: MIDP 1.0
 */
public class TextField extends Item{
    /**
     * The user is allowed to enter any text.
     * may be entered.
     * Constant 0 is assigned to ANY.
     * See Also:Constant Field Values
     */
    public static final int ANY=0;

    /**
     * The mask value for determining the constraint mode. The application should use the bit-wise AND operation with a value returned by getConstraints() and CONSTRAINT_MASK in order to retrieve the current constraint mode, in order to remove any modifier flags such as the PASSWORD flag.
     * Constant 0xFFFF is assigned to CONSTRAINT_MASK.
     * See Also:Constant Field Values
     */
    public static final int CONSTRAINT_MASK=65535;

    /**
     * The user is allowed to enter numeric values with optional decimal fractions, for example
     * -123
     * ,
     * 0.123
     * , or
     * .5
     * .
     * The implementation may display a period . or a comma , for the decimal fraction separator, depending on the conventions in use on the device. Similarly, the implementation may display other device-specific characters as part of a decimal string, such as spaces or commas for digit separators. However, the only characters allowed in the actual contents of the text object are period ., minus sign -, and the decimal digits.
     * The actual contents of a DECIMAL text object may be empty. If the actual contents are not empty, they must conform to a subset of the syntax for a FloatingPointLiteral as defined by the Java Language Specification, section 3.10.2. This subset syntax is defined as follows: the actual contents must consist of an optional minus sign -, followed by one or more whole-number decimal digits, followed by an optional fraction separator, followed by zero or more decimal fraction digits. The whole-number decimal digits may be omitted if the fraction separator and one or more decimal fraction digits are present.
     * The syntax defined above is also enforced whenever the application attempts to set or modify the contents of the text object by calling a constructor or a method.
     * Parsing this string value into a numeric value suitable for computation is the responsibility of the application. If the contents are not empty, the result can be parsed successfully by Double.valueOf and related methods if they are present in the runtime environment.
     * The sign and the fraction separator consume space in the text object. Applications should account for this when assigning a maximum size for the text object.
     * Constant 5 is assigned to DECIMAL.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int DECIMAL=5;

    /**
     * The user is allowed to enter an e-mail address.
     * Constant 1 is assigned to EMAILADDR.
     * See Also:Constant Field Values
     */
    public static final int EMAILADDR=1;

    /**
     * This flag is a hint to the implementation that during text editing, the initial letter of each sentence should be capitalized. This hint should be honored only on devices for which automatic capitalization is appropriate and when the character set of the text being edited has the notion of upper case and lower case letters. The definition of sentence boundaries is implementation-specific.
     * If the application specifies both the INITIAL_CAPS_WORD and the INITIAL_CAPS_SENTENCE flags, INITIAL_CAPS_WORD behavior should be used.
     * The INITIAL_CAPS_SENTENCE modifier can be combined with other input constraints by using the bit-wise OR operator (|).
     * Constant 0x200000 is assigned to INITIAL_CAPS_SENTENCE.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int INITIAL_CAPS_SENTENCE=2097152;

    /**
     * This flag is a hint to the implementation that during text editing, the initial letter of each word should be capitalized. This hint should be honored only on devices for which automatic capitalization is appropriate and when the character set of the text being edited has the notion of upper case and lower case letters. The definition of word boundaries is implementation-specific.
     * If the application specifies both the INITIAL_CAPS_WORD and the INITIAL_CAPS_SENTENCE flags, INITIAL_CAPS_WORD behavior should be used.
     * The INITIAL_CAPS_WORD modifier can be combined with other input constraints by using the bit-wise OR operator (|).
     * Constant 0x100000 is assigned to INITIAL_CAPS_WORD.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int INITIAL_CAPS_WORD=1048576;

    /**
     * Indicates that the text entered does not consist of words that are likely to be found in dictionaries typically used by predictive input schemes. If this bit is clear, the implementation is allowed to (but is not required to) use predictive input facilities. If this bit is set, the implementation should not use any predictive input facilities, but it instead should allow character-by-character text entry.
     * The NON_PREDICTIVE modifier can be combined with other input constraints by using the bit-wise OR operator (|).
     * Constant 0x80000 is assigned to NON_PREDICTIVE.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int NON_PREDICTIVE=524288;

    /**
     * The user is allowed to enter only an integer value. The implementation must restrict the contents either to be empty or to consist of an optional minus sign followed by a string of one or more decimal numerals. Unless the value is empty, it will be successfully parsable using
     * .
     * The minus sign consumes space in the text object. It is thus impossible to enter negative numbers into a text object whose maximum size is 1.
     * Constant 2 is assigned to NUMERIC.
     * See Also:Constant Field Values
     */
    public static final int NUMERIC=2;

    /**
     * Indicates that the text entered is confidential data that should be obscured whenever possible. The contents may be visible while the user is entering data. However, the contents must never be divulged to the user. In particular, the existing contents must not be shown when the user edits the contents. The means by which the contents are obscured is implementation-dependent. For example, each character of the data might be masked with a
     * *
     * character. The PASSWORD modifier is useful for entering confidential information such as passwords or personal identification numbers (PINs).
     * Data entered into a PASSWORD field is treated similarly to SENSITIVE in that the implementation must never store the contents into a dictionary or table for use in predictive, auto-completing, or other accelerated input schemes. If the PASSWORD bit is set in a constraint value, the SENSITIVE and NON_PREDICTIVE bits are also considered to be set, regardless of their actual values. In addition, the INITIAL_CAPS_WORD and INITIAL_CAPS_SENTENCE flag bits should be ignored even if they are set.
     * The PASSWORD modifier can be combined with other input constraints by using the bit-wise OR operator (|). The PASSWORD modifier is not useful with some constraint values such as EMAILADDR, PHONENUMBER, and URL. These combinations are legal, however, and no exception is thrown if such a constraint is specified.
     * Constant 0x10000 is assigned to PASSWORD.
     * See Also:Constant Field Values
     */
    public static final int PASSWORD=65536;

    /**
     * The user is allowed to enter a phone number. The phone number is a special case, since a phone-based implementation may be linked to the native phone dialing application. The implementation may automatically start a phone dialer application that is initialized so that pressing a single key would be enough to make a call. The call must not made automatically without requiring user's confirmation. Implementations may also provide a feature to look up the phone number in the device's phone or address database.
     * The exact set of characters allowed is specific to the device and to the device's network and may include non-numeric characters, such as a + prefix character.
     * Some platforms may provide the capability to initiate voice calls using the MIDlet.platformRequest method.
     * Constant 3 is assigned to PHONENUMBER.
     * See Also:Constant Field Values
     */
    public static final int PHONENUMBER=3;

    /**
     * Indicates that the text entered is sensitive data that the implementation must never store into a dictionary or table for use in predictive, auto-completing, or other accelerated input schemes. A credit card number is an example of sensitive data.
     * The SENSITIVE modifier can be combined with other input constraints by using the bit-wise OR operator (|).
     * Constant 0x40000 is assigned to SENSITIVE.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int SENSITIVE=262144;

    /**
     * Indicates that editing is currently disallowed. When this flag is set, the implementation must prevent the user from changing the text contents of this object. The implementation should also provide a visual indication that the object's text cannot be edited. The intent of this flag is that this text object has the potential to be edited, and that there are circumstances where the application will clear this flag and allow the user to edit the contents.
     * The UNEDITABLE modifier can be combined with other input constraints by using the bit-wise OR operator (|).
     * Constant 0x20000 is assigned to UNEDITABLE.
     * Since: MIDP 2.0 See Also:Constant Field Values
     */
    public static final int UNEDITABLE=131072;

    /**
     * The user is allowed to enter a URL.
     * Constant 4 is assigned to URL.
     * See Also:Constant Field Values
     */
    public static final int URL=4;

    /**
     * Creates a new TextField object with the given label, initial contents, maximum size in characters, and constraints. If the text parameter is null, the TextField is created empty. The maxSize parameter must be greater than zero. An IllegalArgumentException is thrown if the length of the initial contents string exceeds maxSize. However, the implementation may assign a maximum size smaller than the application had requested. If this occurs, and if the length of the contents exceeds the newly assigned maximum size, the contents are truncated from the end in order to fit, and no exception is thrown.
     * label - item labeltext - the initial contents, or null if the TextField is to be emptymaxSize - the maximum capacity in charactersconstraints - see
     * - if maxSize is zero or less
     * - if the value of the constraints parameter is invalid
     * - if text is illegal for the specified constraints
     * - if the length of the string exceeds the requested maximum capacity
     */
    public TextField(java.lang.String label, java.lang.String text, int maxSize, int constraints){
         //TODO codavaj!!
    }

    /**
     * Deletes characters from the TextField.
     * The offset and length parameters must specify a valid range of characters within the contents of the TextField. The offset parameter must be within the range [0..(size())], inclusive. The length parameter must be a non-negative integer such that (offset + length) = size().
     */
    public void delete(int offset, int length){
        return; //TODO codavaj!!
    }

    /**
     * Gets the current input position. For some UIs this may block and ask the user for the intended caret position, and on other UIs this may simply return the current caret position.
     */
    public int getCaretPosition(){
        return 0; //TODO codavaj!!
    }

    /**
     * Copies the contents of the TextField into a character array starting at index zero. Array elements beyond the characters copied are left unchanged.
     */
    public int getChars(char[] data){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the current input constraints of the TextField.
     */
    public int getConstraints(){
        return 0; //TODO codavaj!!
    }

    /**
     * Returns the maximum size (number of characters) that can be stored in this TextField.
     */
    public int getMaxSize(){
        return 0; //TODO codavaj!!
    }

    /**
     * Gets the contents of the TextField as a string value.
     */
    public java.lang.String getString(){
        return null; //TODO codavaj!!
    }

    /**
     * Inserts a subrange of an array of characters into the contents of the TextField. The offset and length parameters indicate the subrange of the data array to be used for insertion. Behavior is otherwise identical to
     * .
     * The offset and length parameters must specify a valid range of characters within the character array data. The offset parameter must be within the range [0..(data.length)], inclusive. The length parameter must be a non-negative integer such that (offset + length) = data.length.
     */
    public void insert(char[] data, int offset, int length, int position){
        return; //TODO codavaj!!
    }

    /**
     * Inserts a string into the contents of the TextField. The string is inserted just prior to the character indicated by the position parameter, where zero specifies the first character of the contents of the TextField. If position is less than or equal to zero, the insertion occurs at the beginning of the contents, thus effecting a prepend operation. If position is greater than or equal to the current size of the contents, the insertion occurs immediately after the end of the contents, thus effecting an append operation. For example, text.insert(s, text.size()) always appends the string s to the current contents.
     * The current size of the contents is increased by the number of inserted characters. The resulting string must fit within the current maximum capacity.
     * If the application needs to simulate typing of characters it can determining the location of the current insertion point (caret) using the with getCaretPosition() method. For example, text.insert(s, text.getCaretPosition()) inserts the string s at the current caret position.
     */
    public void insert(java.lang.String src, int position){
        return; //TODO codavaj!!
    }

    /**
     * Sets the contents of the TextField from a character array, replacing the previous contents. Characters are copied from the region of the data array starting at array index offset and running for length characters. If the data array is null, the TextField is set to be empty and the other parameters are ignored.
     * The offset and length parameters must specify a valid range of characters within the character array data. The offset parameter must be within the range [0..(data.length)], inclusive. The length parameter must be a non-negative integer such that (offset + length) = data.length.
     */
    public void setChars(char[] data, int offset, int length){
        return; //TODO codavaj!!
    }

    /**
     * Sets the input constraints of the TextField. If the the current contents of the TextField do not match the new constraints, the contents are set to empty.
     */
    public void setConstraints(int constraints){
        return; //TODO codavaj!!
    }

    /**
     * Sets a hint to the implementation as to the input mode that should be used when the user initiates editing of this TextField. The characterSubset parameter names a subset of Unicode characters that is used by the implementation to choose an initial input mode. If null is passed, the implementation should choose a default input mode.
     * See Input Modes for a full explanation of input modes.
     */
    public void setInitialInputMode(java.lang.String characterSubset){
        return; //TODO codavaj!!
    }

    /**
     * Sets the maximum size (number of characters) that can be contained in this TextField. If the current contents of the TextField are larger than maxSize, the contents are truncated to fit.
     */
    public int setMaxSize(int maxSize){
        return 0; //TODO codavaj!!
    }

    /**
     * Sets the contents of the TextField as a string value, replacing the previous contents.
     */
    public void setString(java.lang.String text){
        return; //TODO codavaj!!
    }

    /**
     * Gets the number of characters that are currently stored in this TextField.
     */
    public int size(){
        return 0; //TODO codavaj!!
    }

}
