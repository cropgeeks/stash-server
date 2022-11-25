/*
 * Copyright 2018 Information & Computational Sciences, The James Hutton Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Sebastian Raubach
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Token
{
	private String  token;
	private String  imageToken;
	private Integer id;
	private String  fullName;
	private String  email;
	private Long    lifetime;
	private Long    createdOn;
}
