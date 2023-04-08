/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.annotation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

/**
 * Internal class for managing injection metadata.
 *
 * <p>Not intended for direct use in applications.
 *
 * <p>Used by {@link AutowiredAnnotationBeanPostProcessor},
 * {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor}, and
 * {@link org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor}.
 *
 * @author Juergen Hoeller
 * @since 2.5
 */
public class InjectionMetadata {

	/**
	 * An empty {@code InjectionMetadata} instance with no-op callbacks.
	 * @since 5.2
	 */
	public static final InjectionMetadata EMPTY = new InjectionMetadata(Object.class, Collections.emptyList()) {
		@Override
		protected boolean needsRefresh(Class<?> clazz) {
			return false;
		}
		@Override
		public void checkConfigMembers(RootBeanDefinition beanDefinition) {
		}
		@Override
		public void inject(Object target, @Nullable String beanName, @Nullable PropertyValues pvs) {
		}
		@Override
		public void clear(@Nullable PropertyValues pvs) {
		}
	};


	private final Class<?> targetClass;

	private final Collection<InjectedElement> injectedElements;

	@Nullable
	private volatile Set<InjectedElement> checkedElements;


	/**
	 * Create a new {@code InjectionMetadata instance}.
	 * <p>Preferably use {@link #forElements} for reusing the {@link #EMPTY}
	 * instance in case of no elements.
	 * @param targetClass the target class
	 * @param elements the associated elements to inject
	 * @see #forElements
	 */
	public InjectionMetadata(Class<?> targetClass, Collection<InjectedElement> elements) {
		this.targetClass = targetClass;
		this.injectedElements = elements;
	}


	/**
	 * Return the {@link InjectedElement elements} to inject.
	 * @return the elements to inject
	 */
	public Collection<InjectedElement> getInjectedElements() {
		return Collections.unmodifiableCollection(this.injectedElements);
	}

	/**
	 * Determine whether this metadata instance needs to be refreshed.
	 * @param clazz the current target class
	 * @return {@code true} indicating a refresh, {@code false} otherwise
	 * @since 5.2.4
	 */
	protected boolean needsRefresh(Class<?> clazz) {
		return this.targetClass != clazz;
	}

	public void checkConfigMembers(RootBeanDefinition beanDefinition) {
		if (this.injectedElements.isEmpty()) {
			this.checkedElements = Collections.emptySet();
		}
		else {
			Set<InjectedElement> checkedElements = new LinkedHashSet<>((this.injectedElements.size() * 4 / 3) + 1);
			for (InjectedElement element : this.injectedElements) {
				Member member = element.getMember();
				if (!beanDefinition.isExternallyManagedConfigMember(member)) {
					beanDefinition.registerExternallyManagedConfigMember(member);
					checkedElements.add(element);
				}
			}
			this.checkedElements = checkedElements;
		}
	}

	/**
	 * 主要执行以下步骤：
	 *
	 * 首先判断是否存在需要被注入的属性对象。
	 *
	 * 对所有需要被注入的属性对象逐一调用InjectedElement实例对象的inject方法进行属性注入。
	 *
	 * 其中，如果需要被注入的属性类型与容器中有多个实例对象的类型相同，则会抛出NoUniqueBeanDefinitionException异常。
	 *
	 * 如果在属性注入过程中发生了异常，则该异常将被封装为BeanCreationException并抛出。
	 *
	 * @param target 需要被依赖注入的目标对象
	 * @param beanName 对应的bean名称
	 * @param pvs 当前Bean的PropertyValues对象
	 * @throws Throwable
	 */
	public void inject(Object target, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
		Collection<InjectedElement> checkedElements = this.checkedElements;
		// 如果有缓存的InjectionMetadata，则使用它，否则使用当前实例中的injectedElements属性
		Collection<InjectedElement> elementsToIterate =
				(checkedElements != null ? checkedElements : this.injectedElements);
		// 如果存在要进行注入操作的元素，则遍历这些元素，对目标对象的属性进行依赖注入
		if (!elementsToIterate.isEmpty()) {
			for (InjectedElement element : elementsToIterate) {
				// 调用InjectedElement实例对象中保存的依赖注入信息完成属性注入操作
				/** @see AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement#inject(Object, String, PropertyValues) */
				element.inject(target, beanName, pvs);
			}
		}
	}

	/**
	 * Clear property skipping for the contained elements.
	 * @since 3.2.13
	 */
	public void clear(@Nullable PropertyValues pvs) {
		Collection<InjectedElement> checkedElements = this.checkedElements;
		Collection<InjectedElement> elementsToIterate =
				(checkedElements != null ? checkedElements : this.injectedElements);
		if (!elementsToIterate.isEmpty()) {
			for (InjectedElement element : elementsToIterate) {
				element.clearPropertySkipping(pvs);
			}
		}
	}


	/**
	 * Return an {@code InjectionMetadata} instance, possibly for empty elements.
	 * @param elements the elements to inject (possibly empty)
	 * @param clazz the target class
	 * @return a new {@link #InjectionMetadata(Class, Collection)} instance
	 * @since 5.2
	 */
	public static InjectionMetadata forElements(Collection<InjectedElement> elements, Class<?> clazz) {
		return (elements.isEmpty() ? new InjectionMetadata(clazz, Collections.emptyList()) :
				new InjectionMetadata(clazz, elements));
	}

	/**
	 * Check whether the given injection metadata needs to be refreshed.
	 * @param metadata the existing metadata instance
	 * @param clazz the current target class
	 * @return {@code true} indicating a refresh, {@code false} otherwise
	 * @see #needsRefresh(Class)
	 */
	public static boolean needsRefresh(@Nullable InjectionMetadata metadata, Class<?> clazz) {
		return (metadata == null || metadata.needsRefresh(clazz));
	}


	/**
	 * A single injected element.
	 */
	public abstract static class InjectedElement {

		protected final Member member;

		protected final boolean isField;

		@Nullable
		protected final PropertyDescriptor pd;

		@Nullable
		protected volatile Boolean skip;

		protected InjectedElement(Member member, @Nullable PropertyDescriptor pd) {
			this.member = member;
			this.isField = (member instanceof Field);
			this.pd = pd;
		}

		public final Member getMember() {
			return this.member;
		}

		protected final Class<?> getResourceType() {
			if (this.isField) {
				return ((Field) this.member).getType();
			}
			else if (this.pd != null) {
				return this.pd.getPropertyType();
			}
			else {
				return ((Method) this.member).getParameterTypes()[0];
			}
		}

		protected final void checkResourceType(Class<?> resourceType) {
			if (this.isField) {
				Class<?> fieldType = ((Field) this.member).getType();
				if (!(resourceType.isAssignableFrom(fieldType) || fieldType.isAssignableFrom(resourceType))) {
					throw new IllegalStateException("Specified field type [" + fieldType +
							"] is incompatible with resource type [" + resourceType.getName() + "]");
				}
			}
			else {
				Class<?> paramType =
						(this.pd != null ? this.pd.getPropertyType() : ((Method) this.member).getParameterTypes()[0]);
				if (!(resourceType.isAssignableFrom(paramType) || paramType.isAssignableFrom(resourceType))) {
					throw new IllegalStateException("Specified parameter type [" + paramType +
							"] is incompatible with resource type [" + resourceType.getName() + "]");
				}
			}
		}

		/**
		 * Either this or {@link #getResourceToInject} needs to be overridden.
		 *
		 * 通过反射调用目标对象的set方法或invoke方法，将对应的依赖资源注入到目标对象的属性中。 具体而言：
		 *
		 * 如果当前元素对应的成员是一个属性，即this.isField为true，则将该属性设置为可访问状态，
		 * 并使用ReflectionUtils工具类中的makeAccessible()方法处理这个成员变量，将其设置为可以访问。
		 * 然后，通过field.set()方法将依赖资源注入到目标对象中。
		 *
		 * 如果当前元素对应的成员是一个方法，即this.isField为false，则首先检查是否需要跳过该属性。
		 * 如果需要，直接返回。否则，使用ReflectionUtils工具类中的makeAccessible()方法处理这个方法，
		 * 将其设置为可以访问。然后，通过method.invoke()方法调用目标方法，并传入目标对象和对应的依赖资源。
		 * 如果调用方法时发生异常，则通过捕获InvocationTargetException异常并重新抛出其目标异常来处理异常情况。
		 *
		 */
		protected void inject(Object target, @Nullable String requestingBeanName, @Nullable PropertyValues pvs)
				throws Throwable {

			// 如果当前元素为属性，则将其设置为访问可见性，并将依赖资源注入到目标对象中
			if (this.isField) {
				Field field = (Field) this.member;
				// 将成员变量设置为访问可见性
				ReflectionUtils.makeAccessible(field);
				field.set(target, getResourceToInject(target, requestingBeanName));
			}
			else {// 当前元素为方法
				// 检查是否需要跳过该属性
				if (checkPropertySkipping(pvs)) {
					// 如果需要跳过，则直接返回
					return;
				}
				try {
					Method method = (Method) this.member;
					// 将方法设置为访问可见性
					ReflectionUtils.makeAccessible(method);
					// 调用方法，并传入对应的依赖资源
					method.invoke(target, getResourceToInject(target, requestingBeanName));
				}
				catch (InvocationTargetException ex) {
					// 如果调用方法时发生异常，则捕获并重新抛出封装后的异常
					throw ex.getTargetException();
				}
			}
		}

		/**
		 * Check whether this injector's property needs to be skipped due to
		 * an explicit property value having been specified. Also marks the
		 * affected property as processed for other processors to ignore it.
		 */
		protected boolean checkPropertySkipping(@Nullable PropertyValues pvs) {
			Boolean skip = this.skip;
			if (skip != null) {
				return skip;
			}
			if (pvs == null) {
				this.skip = false;
				return false;
			}
			synchronized (pvs) {
				skip = this.skip;
				if (skip != null) {
					return skip;
				}
				if (this.pd != null) {
					if (pvs.contains(this.pd.getName())) {
						// Explicit value provided as part of the bean definition.
						this.skip = true;
						return true;
					}
					else if (pvs instanceof MutablePropertyValues mpvs) {
						mpvs.registerProcessedProperty(this.pd.getName());
					}
				}
				this.skip = false;
				return false;
			}
		}

		/**
		 * Clear property skipping for this element.
		 * @since 3.2.13
		 */
		protected void clearPropertySkipping(@Nullable PropertyValues pvs) {
			if (pvs == null) {
				return;
			}
			synchronized (pvs) {
				if (Boolean.FALSE.equals(this.skip) && this.pd != null && pvs instanceof MutablePropertyValues mpvs) {
					mpvs.clearProcessedProperty(this.pd.getName());
				}
			}
		}

		/**
		 * Either this or {@link #inject} needs to be overridden.
		 */
		@Nullable
		protected Object getResourceToInject(Object target, @Nullable String requestingBeanName) {
			return null;
		}

		@Override
		public boolean equals(@Nullable Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof InjectedElement otherElement)) {
				return false;
			}
			return this.member.equals(otherElement.member);
		}

		@Override
		public int hashCode() {
			return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + " for " + this.member;
		}
	}

}
