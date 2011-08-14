/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uniluebeck.sourcegen.java;

import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;

public interface JInterfaceMethod extends JLangElem {

	class JavaInterfaceMethodFactory {

		private static JavaInterfaceMethodFactory instance;

		private JavaInterfaceMethodFactory() { /* not to be invoked */ }

		static JavaInterfaceMethodFactory getInstance() {
			if (instance == null)
				instance = new JavaInterfaceMethodFactory();
			return instance;
		}

		public JInterfaceMethod create(int modifiers, String returnType, String name,
				JMethodSignature signature, String[] exceptions) throws JDuplicateException,
				JInvalidModifierException {

			return new JInterfaceMethodImpl(modifiers, returnType, name, signature, exceptions);
		}

	}

	public static final JavaInterfaceMethodFactory factory = JavaInterfaceMethodFactory.getInstance();

	public JInterfaceMethod add					(JParameter... params) 	throws JDuplicateException;

	public JInterfaceMethod addException		(String... exception) 	throws JDuplicateException;
	public boolean 			containsException	(String exception)		;
	public JMethodSignature	getSignature		();
	public boolean 			equals				(JInterfaceMethod other);

	/**
	 * Adds a annotation to this method. Supplying the at sign (@) is not
	 * necessary since it is added automatically. Annotation containing
	 * parameters (e.g. <code>@SuppressWarnings("unused")</code>) are not yet
	 * supported.
	 *
	 * @param annotations The annotation's name.
	 * @return This object.
	 */
	public JInterfaceMethod addAnnotation       (String... annotations);
	/**
	 * Set the Javadoc comment for the current method.
	 *
	 * @param comment The Java method comment.
	 * @return This object.
	 */
	public JInterfaceMethod setComment			(JMethodComment comment);

  /**
	 * Set the annotation for the current method.
	 *
	 * @param annotation The Java method annotation.
	 * @return This object.
	 */
	public JInterfaceMethod setAnnotation			(JMethodAnnotation annotation);
}
