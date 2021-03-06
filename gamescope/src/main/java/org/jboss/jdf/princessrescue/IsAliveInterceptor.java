package org.jboss.jdf.princessrescue;

import java.io.Serializable;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@IsAlive
@Interceptor
public class IsAliveInterceptor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	@Current
	Instance<Player> currentPlayer;

	@AroundInvoke
	public Object manage(InvocationContext ic) throws Exception {
		
		if (!currentPlayer.get().isShot()) {
			// I'am doing science and I'am still alive!	
			return ic.proceed();
		}
		
		return null;
	}

}
