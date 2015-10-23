package Library.entities;

package library.entities;

import library.entities.Member;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMember {


    @Test
    public void IDLessThanZero() throws Exception {

        try {
            Member member = new Member("something", "something", "soething", "something", -22);
        } catch (Exception e) {
            return;
        }
        fail();
    }


}