/*
 * The MIT License
 * Copyright © 2018 Phillip Schichtel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tel.schich.javacan;

import static tel.schich.javacan.LinuxErrnoBase.EAGAIN;

public class OSError {
    public final int errorNumber;
    public final String errorMessage;

    public OSError(int errorNumber, String errorMessage) {
        this.errorNumber = errorNumber;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "OSError{" + "errorNumber=" + errorNumber + ", errorMessage='" + errorMessage + '\'' + '}';
    }

    public static OSError getLast() {
        int lastErrno = SocketCAN.errno();
        if (lastErrno == 0) {
            return null;
        }

        return new OSError(lastErrno, SocketCAN.errstr(lastErrno));
    }

    private static boolean isTemporary(int errno) {
        switch (errno) {
        case EAGAIN:
            return true;
        default:
            return false;
        }
    }

    public boolean mayTryAgain() {
        return isTemporary(errorNumber);
    }

    public static boolean wasTemporaryError() {
        return isTemporary(SocketCAN.errno());
    }
}
