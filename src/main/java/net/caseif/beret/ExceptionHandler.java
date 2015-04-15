/*
 * New BSD License (BSD-new)
 *
 * Copyright (c) 2015 Maxim Roncacé
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the copyright holder nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.caseif.beret;

/**
 * Represents an exception handler within a method.
 *
 * @author Max Roncacé
 */
public class ExceptionHandler {

	private MethodInfo parent;
	private int startIndex;
	private int endIndex;
	private int handlerStart;
	private String catchType;

	/**
	 * Instantiates a new {@link ExceptionHandler} with the given information.
	 *
	 * @param parent The parent {@link MethodInfo}
	 * @param start The index within the method's code array at which this
	 *              {@link ExceptionHandler} becomes active
	 * @param end The index within the method's code array at which this
	 *              {@link ExceptionHandler} ends
	 * @param handlerStart The index within the method's code array at which
	 *                     this {@link ExceptionHandler} begins
	 * @param catchType The class representing the exception this
	 *                  {@link ExceptionHandler} is designated to catch
	 */
	public ExceptionHandler(MethodInfo parent, int start, int end, int handlerStart, String catchType) {
		if (end <= start) {
			throw new IllegalArgumentException("Exception handler end index must be greater than start index");
		}
		this.parent = parent;
		this.startIndex = start;
		this.endIndex = end;
		this.handlerStart = handlerStart;
		this.catchType = catchType;
	}

	public MethodInfo getParent() {
		return this.parent;
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public int getEndIndex() {
		return this.endIndex;
	}

	public int getHandlerStartIndex() {
		return this.handlerStart;
	}

	public String getCatchType() {
		return this.catchType;
	}

}
