
package library.interfaces.daos;

import java.util.List;
import library.interfaces.entities.IMember;

public interface IMemberDAO {
	IMember addMember(String var1, String var2, String var3, String var4);

	IMember getMemberByID(int var1);

	List<IMember> listMembers();

	List<IMember> findMembersByLastName(String var1);

	List<IMember> findMembersByEmailAddress(String var1);

	List<IMember> findMembersByNames(String var1, String var2);
}
