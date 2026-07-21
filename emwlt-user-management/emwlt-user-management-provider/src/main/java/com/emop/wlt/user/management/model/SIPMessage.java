package com.emop.wlt.user.management.model;

import cfca.sadk.org.bouncycastle.asn1.ASN1Encodable;
import cfca.sadk.org.bouncycastle.asn1.ASN1Integer;
import cfca.sadk.org.bouncycastle.asn1.ASN1Sequence;
import cfca.sadk.org.bouncycastle.asn1.ASN1TaggedObject;
import cfca.sadk.org.bouncycastle.asn1.DEROctetString;
import cfca.sadk.org.bouncycastle.asn1.DERUTF8String;
import cfca.sadk.org.bouncycastle.util.Strings;
import cfca.sadk.org.bouncycastle.util.encoders.Base64;
import lombok.Getter;

/**
 * @author bobo
 * @Description:
 * @date 2022/4/7
 */
@Getter
public class SIPMessage {

    private int version;
    private byte[] publickeyHash;
    private byte[] serverRandom;
    private byte[] clientRandomCipher;
    private byte[] cipherText;
    private String extInfo;
    private int algMode = 0;

    private SIPMessage() {
    }

    private SIPMessage(ASN1Sequence seq) throws IllegalArgumentException {
        if (seq == null) {
            throw new IllegalArgumentException("SIPMessage: sequence argument missing");
        } else {
            int size = seq.size();
            if (size != 6 && size != 7) {
                throw new IllegalArgumentException("SIPMessage: sequence wrong size for object-6/7");
            } else {
                this.version = ASN1Integer.getInstance(seq.getObjectAt(0)).getValue().intValue();
                this.publickeyHash = DEROctetString.getInstance(seq.getObjectAt(1)).getOctets();
                this.serverRandom = DEROctetString.getInstance(seq.getObjectAt(2)).getOctets();
                this.clientRandomCipher = DEROctetString.getInstance(seq.getObjectAt(3)).getOctets();
                this.cipherText = DEROctetString.getInstance(seq.getObjectAt(4)).getOctets();
                this.extInfo = DERUTF8String.getInstance(seq.getObjectAt(5)).getString();
                if (size == 7) {
                    ASN1Encodable asn1 = seq.getObjectAt(6);
                    ASN1Integer value = null;
                    if (asn1 instanceof ASN1TaggedObject) {
                        value = ASN1Integer.getInstance((ASN1TaggedObject) asn1, true);
                    } else {
                        if (!(asn1 instanceof ASN1Integer)) {
                            throw new IllegalArgumentException("SIPMessage: algModeInvalid");
                        }

                        value = ASN1Integer.getInstance(asn1);
                    }

                    this.algMode = value.getValue().intValue();
                }

                if (this.publickeyHash != null && this.publickeyHash.length == 32) {
                    if (this.serverRandom == null || this.serverRandom.length != 16) {
                        throw new IllegalArgumentException("SIPMessage: serverRandomHash==null/length!=16");
                    }
                } else {
                    throw new IllegalArgumentException("SIPMessage: publickeyHash==null/length!=32");
                }
            }
        }
    }

    public static SIPMessage decode(String message) throws IllegalArgumentException {
        if (message == null) {
            throw new IllegalArgumentException("message==null");
        } else {
            byte[] data = Base64.decode(message);
            ASN1Sequence asn1 = ASN1Sequence.getInstance(data);
            return new SIPMessage(asn1);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(256);
        builder.append("SIPMessage[");
        builder.append("version=").append(this.version);
        builder.append("publickeyHash=").append(this.dump(this.publickeyHash));
        builder.append("serverRandomHash=").append(this.dump(this.serverRandom));
        builder.append("clientRandomCipher=").append(this.dump(this.clientRandomCipher));
        builder.append("cipherText=").append(this.dump(this.cipherText));
        builder.append("extInfo=").append(this.extInfo);
        if (this.algMode != 0) {
            builder.append("algMode=").append(this.algMode);
        }

        return builder.toString();
    }

    private String dump(byte[] value) {
        String valueResult = null;
        if (value == null) {
            valueResult = "NONE";
        } else if (value.length <= 256) {
            valueResult = Strings.fromByteArray(Base64.encode(value));
        } else {
            valueResult = Strings.fromByteArray(Base64.encode(value, 0, 256)) + "...L=" + value.length;
        }

        return valueResult;
    }
}
