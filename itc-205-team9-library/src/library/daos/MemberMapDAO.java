package library.daos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;

public class MemberMapDAO implements IMemberDAO {
    private IMemberHelper helper;
    private Map<Integer, IMember> memberMap;
    private int nextID;

    public MemberMapDAO(IMemberHelper helper) {
        if(helper == null) {
            throw new IllegalArgumentException(String.format("MemberMapDAO : constructor : helper cannot be null.", new Object[0]));
        } else {
            this.helper = helper;
            this.memberMap = new HashMap();
            this.nextID = 1;
        }
    }

    public MemberMapDAO(IMemberHelper helper, Map<Integer, IMember> memberMap) {
        this(helper);
        if(memberMap == null) {
            throw new IllegalArgumentException(String.format("MemberMapDAO : constructor : memberMap cannot be null.", new Object[0]));
        } else {
            this.memberMap = memberMap;
        }
    }

    public IMember addMember(String firstName, String lastName, String contactPhone, String emailAddress) {
        int id = this.getNextId();
        IMember mem = this.helper.makeMember(firstName, lastName, contactPhone, emailAddress, id);
        this.memberMap.put(Integer.valueOf(id), mem);
        return mem;
    }

    public IMember getMemberByID(int id) {
        return this.memberMap.keySet().contains(Integer.valueOf(id))?(IMember)this.memberMap.get(Integer.valueOf(id)):null;
    }

    public List<IMember> listMembers() {
        ArrayList list = new ArrayList(this.memberMap.values());
        return Collections.unmodifiableList(list);
    }

    public List<IMember> findMembersByLastName(String lastName) {
        if(lastName != null && !lastName.isEmpty()) {
            ArrayList list = new ArrayList();
            Iterator var4 = this.memberMap.values().iterator();

            while(var4.hasNext()) {
                IMember m = (IMember)var4.next();
                if(lastName.equals(m.getLastName())) {
                    list.add(m);
                }
            }

            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(String.format("MemberMapDAO : findMembersByLastName : lastName cannot be null or blank", new Object[0]));
        }
    }

    public List<IMember> findMembersByEmailAddress(String emailAddress) {
        if(emailAddress != null && !emailAddress.isEmpty()) {
            ArrayList list = new ArrayList();
            Iterator var4 = this.memberMap.values().iterator();

            while(var4.hasNext()) {
                IMember m = (IMember)var4.next();
                if(emailAddress.equals(m.getEmailAddress())) {
                    list.add(m);
                }
            }

            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(String.format("MemberMapDAO : findMembersByEmailAddress : emailAddress cannot be null or blank", new Object[0]));
        }
    }

    public List<IMember> findMembersByNames(String firstName, String lastName) {
        if(firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
            ArrayList list = new ArrayList();
            Iterator var5 = this.memberMap.values().iterator();

            while(var5.hasNext()) {
                IMember m = (IMember)var5.next();
                if(firstName.equals(m.getFirstName()) && lastName.equals(m.getLastName())) {
                    list.add(m);
                }
            }

            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(String.format("MemberMapDAO : findMembersByNames : firstName and lastName cannot be null or blank", new Object[0]));
        }
    }

    private int getNextId() {
        return this.nextID++;
    }
}