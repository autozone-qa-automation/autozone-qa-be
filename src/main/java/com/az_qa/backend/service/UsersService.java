/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.az_qa.backend.dao.UsersDAO;
import com.az_qa.backend.exception.DuplicatedItemException;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.MissingRequiredFieldException;
import com.az_qa.backend.vo.UserVO;

/**
 * Service contract for user management operations.
 */
@Service
public class UsersService {

  @Autowired private UsersDAO userDAO;

  /**
   * Adds a new user to the persistence layer.
   *
   * @param userVO user payload to persist
   * @return persisted user representation or {@code null} when the input is
   *         {@code null}
   * @throws MissingRequiredFieldException when {@code producerId} is missing
   *                                       for user creation
   */
  public UserVO add(UserVO userVO) {
    if (userVO.getRoleId() == null) {
      throw new MissingRequiredFieldException("Producer id is required for user creation.");
    }
    if (userVO.getEmail() == null) {
      throw new MissingRequiredFieldException("Email is required for user creation.");
    }
    if (userVO.getPassword() == null) {
      throw new MissingRequiredFieldException("Password is required for user creation.");
    }
    if (userVO.getName() == null) {
      throw new MissingRequiredFieldException("Name is required for user creation.");
    }
    if (userVO.getLastName() == null) {
      throw new MissingRequiredFieldException("Last name is required for user creation.");
    }
    try {
      userDAO.findByEmail(userVO.getEmail());
      throw new DuplicatedItemException(
          "User with email {" + userVO.getEmail() + "} already exists.");
    } catch (ItemNotFoundException e) {
      return userDAO.add(userVO);
    }
  }

  /**
   * Deactivates a user by id.
   *
   * @param id user id
   * @return no content response if deactivated, not found if user does not exist
   *
   * @throws ItemNotFoundException when no user exists with the provided id
   */
  public void deactivate(Long id) {
    userDAO.deactivate(id);
  }


  /**Notas de desarrollo (H):  
   * Esto será el update un usuario. Esta sección deberá actuar como el controlador de tráfico que: 
   * 1. Verifica que el ID proporcionado exista realmente en el sistema. 
   * 2. Valida que, si el email cambió, no le pertenezca a otra persona. 
   * 3. Utiliza el UserMapper (a través del DAO) para asefurar que el objeto tenga los flags correctos (como el isNew=false) para
   * que JPArealice un UPDATE y no un INSERT. El DAO lo termineré un poco después. 
   * Así, la lógica debe ser: busca si existe el correo; si existe, verifica si el ID del usuario encontrado es diferente al ID del
   * usuario que se quiere actualizar; si los IDs son iguales, significa que es el mismo usuario y el cambio es válido. 
   * Es imperativo que el UserVO que llefa al método update contenga el id para poder realizar la comparación y que DAO sepa
   * qué registro modificar en vez de crear otro otra vez. 
   * 
   */
  /**
   * Updates a user in the persistence layer.
   *
   * @param userVO user payload to persist
   * @return persisted user representation
   * @throws MissingRequiredFieldException when mandatory fields are missing
   * @throws ItemNotFoundException when the user does not exist or is inactive
   * @throws DuplicatedItemException when the email belongs to another user
   */
  public UserVO update(UserVO userVO) {
    // P1:Validación de campos obligatorios (incluye ID)
    if (userVO.getId() == null) {
      throw new MissingRequiredFieldException("User ID is required for update.");
    }
    if (userVO.getRoleId() == null) {
      throw new MissingRequiredFieldException("Role id is required for user update.");
    }
    if (userVO.getEmail() == null) {
      throw new MissingRequiredFieldException("Email is required for user update.");
    }
    if (userVO.getPassword() == null) {
      throw new MissingRequiredFieldException("Password is required for user update.");
    }
    if (userVO.getName() == null) {
      throw new MissingRequiredFieldException("Name is required for user update.");
    }
    if (userVO.getLastName() == null) {
      throw new MissingRequiredFieldException("Last name is required for user update.");
    }
    // P2: Operación de lectura (Verificar existencia y estado activo)
    // Se utiliza el método findByIdAndIsActive[mejor getID, ni al caso] para asegurar que el usuario existe y puede ser editado.
    userDAO.findById(userVO.getId());

    // P3: Validación de email único
    try {
      UserVO existingUserWithEmail = userDAO.findByEmail(userVO.getEmail());

      // Si el email existe, pero el ID es diferente, entonces es un duplicado
      if (!existingUserWithEmail.getId().equals(userVO.getId())) {
        throw new DuplicatedItemException(
            "User with email {" + userVO.getEmail() + "} already exists.");
      }
    } catch (ItemNotFoundException e) {
      // Si no se encuentra ningún usuario con el email, entonces no hay duplicado y está libre woo. 
    }
    
    // P4: Persistencia (mapper debee encargarse de)
    return userDAO.update(userVO);
  }
}
