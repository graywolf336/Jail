package com.graywolf336.jail.enums;

public enum Confirmation {
    /** When they are clearing a jail from all their prisoner's with releasing properly. */
    CLEAR,
    /** When they are clearing a jail from all their prisoner's by force. */
    CLEARFORCE,
    /** When they are deleting a cell from a jail. */
    DELETECELL,
    /** When they are deleting all a jail's cells. */
    DELETECELLS,
    /** When they are deleting a jail. */
    DELETE;
}
