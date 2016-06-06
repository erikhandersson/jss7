package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.BearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author vadim subbotin
 */
public class RequestedSubscriptionInfoTest {
    private byte[] data = {48, 28, -95, 8, 4, 1, 64, -126, 1, 0, -124, 0, -126, 0, -125, 1, 0, -124, 0, -121, 1,
            0, -120, 0, -119, 0, -118, 0, -117, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        RequestedSubscriptionInfoImpl requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl();
        requestedSubscriptionInfo.decodeAll(asn);

        assertNotNull(requestedSubscriptionInfo.getRequestedSSInfo());
        assertTrue(requestedSubscriptionInfo.getOdb());
        assertEquals(requestedSubscriptionInfo.getRequestedCAMELSubscriptionInfo(), RequestedCAMELSubscriptionInfo.oCSI);
        assertTrue(requestedSubscriptionInfo.getSupportedVlrCamelPhases());
        assertFalse(requestedSubscriptionInfo.getSupportedSgsnCamelPhases());
        assertNull(requestedSubscriptionInfo.getExtensionContainer());
        assertEquals(requestedSubscriptionInfo.getAdditionalRequestedCamelSubscriptionInfo(),
                AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI);
        assertTrue(requestedSubscriptionInfo.getMsisdnBsList());
        assertTrue(requestedSubscriptionInfo.getCsgSubscriptionDataRequested());
        assertTrue(requestedSubscriptionInfo.getCwInfo());
        assertTrue(requestedSubscriptionInfo.getClipInfo());
        assertFalse(requestedSubscriptionInfo.getClirInfo());
        assertFalse(requestedSubscriptionInfo.getHoldInfo());
        assertFalse(requestedSubscriptionInfo.getEctInfo());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        BearerServiceCodeImpl bearerServiceCode = new BearerServiceCodeImpl(BearerServiceCodeValue.allBearerServices);
        BasicServiceCodeImpl basicServiceCode = new BasicServiceCodeImpl(bearerServiceCode);
        SSForBSCodeImpl ssForBSCode = new SSForBSCodeImpl(new SSCodeImpl(SupplementaryCodeValue.allCallCompletionSS), basicServiceCode, true);
        RequestedSubscriptionInfoImpl requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl(ssForBSCode, true,
                RequestedCAMELSubscriptionInfo.oCSI, true, false, null, AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI,
                true, true, true, true, false, false, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        requestedSubscriptionInfo.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
